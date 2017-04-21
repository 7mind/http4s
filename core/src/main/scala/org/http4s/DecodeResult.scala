package org.http4s

import cats._
import cats.data._
import cats.implicits._
import fs2.Task

object DecodeResult {
  def apply[F[_], A](fa: F[Either[DecodeFailure, A]]): DecodeResult[F, A] =
    EitherT(fa)

  def success[F[_], A](a: F[A])(implicit F: Functor[F]): DecodeResult[F, A] =
    DecodeResult(a.map(Right(_)))

  def success[F[_], A](a: A)(implicit F: Applicative[F]): DecodeResult[F, A] =
    success(F.pure(a))

  def failure[F[_], A](e: F[DecodeFailure])(implicit F: Functor[F]): DecodeResult[F, A] =
    DecodeResult(e.map(Left(_)))

  def failure[F[_], A](e: DecodeFailure)(implicit F: Applicative[F]): DecodeResult[F, A] =
    failure(F.pure(e))
}
