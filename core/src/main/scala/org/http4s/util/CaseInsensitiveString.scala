package org.http4s.util

import java.util.Locale
import scalaz.{Equal, Monoid, Order, Ordering, Show}

/**
 * A String wrapper such that two strings `x` and `y` are equal if
 * `x.value.equalsIgnoreCase(y.value)`
 */
sealed class CaseInsensitiveString private (val value: String)
    extends CharSequence
    with Ordered[CaseInsensitiveString] {
  import CaseInsensitiveString._

  private var hash = 0

  override def hashCode(): Int = {
    if (hash == 0 && value.length > 0) {
      hash = value.toLowerCase(Locale.ROOT).hashCode
    }
    hash
  }

  override def equals(obj: Any): Boolean = obj match {
    case that: CaseInsensitiveString => value.equalsIgnoreCase(that.value)
    case _ => false
  }

  override def toString: String = value

  override def charAt(n: Int): Char =
    toString.charAt(n)

  override def length(): Int =
    value.length

  override def subSequence(start: Int, end: Int): CaseInsensitiveString =
    apply(value.subSequence(start, end))

  override def compare(other: CaseInsensitiveString): Int =
    value.compareToIgnoreCase(other.value)
}

object CaseInsensitiveString extends CaseInsensitiveStringInstances {
  val empty: CaseInsensitiveString =
    CaseInsensitiveString("")

  def apply(cs: CharSequence): CaseInsensitiveString =
    new CaseInsensitiveString(cs.toString)
}

private[http4s] sealed trait CaseInsensitiveStringInstances {
  implicit val http4sInstancesForCaseInsensitiveString: Monoid[CaseInsensitiveString] with Order[CaseInsensitiveString] with Show[CaseInsensitiveString] =
    new Monoid[CaseInsensitiveString] with Order[CaseInsensitiveString] with Show[CaseInsensitiveString] {
      def zero: CaseInsensitiveString =
        CaseInsensitiveString.empty
      def append(f1: CaseInsensitiveString, f2: => CaseInsensitiveString) =
        CaseInsensitiveString(f1.value + f2.value)

      def order(x: CaseInsensitiveString, y: CaseInsensitiveString): Ordering =
        Ordering.fromInt(x.value compare y.value)
      override def equal(x: CaseInsensitiveString, y: CaseInsensitiveString): Boolean =
        x == y
      override def equalIsNatural: Boolean =
        true

      override def shows(x: CaseInsensitiveString): String =
        x.toString
    }
}
