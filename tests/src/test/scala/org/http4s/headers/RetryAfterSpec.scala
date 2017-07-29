package org.http4s
package headers

import java.time.{Instant, ZoneId, ZonedDateTime}

import scala.concurrent.duration._

class RetryAfterSpec extends HeaderLaws {
  checkAll("Retry-After", headerLaws(`Retry-After`))

  val gmtDate: ZonedDateTime = ZonedDateTime.of(1999, 12, 31, 23, 59, 59, 0, ZoneId.of("GMT"))

  "render" should {
    "format GMT date according to RFC 1123" in {
      `Retry-After`(Left(HttpDate.unsafeFromZonedDateTime(gmtDate))).renderString must_== "Retry-After: Fri, 31 Dec 1999 23:59:59 GMT"
    }
    "duration in seconds" in {
      `Retry-After`(Right(120.seconds)).renderString must_== "Retry-After: 120"
    }
  }

  "parse" should {
    "accept http date" in {
      `Retry-After`.parse("Fri, 31 Dec 1999 23:59:59 GMT").map(_.retry) must be_\/-(Left(HttpDate.unsafeFromZonedDateTime(gmtDate)))
    }
    "accept duration on seconds" in {
      `Retry-After`.parse("120").map(_.retry) must be_\/-(Right(120.seconds))
    }
  }
}
