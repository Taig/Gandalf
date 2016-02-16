package io.taig.bsts.tests

import cats.data.NonEmptyList
import cats.data.Validated.{ Invalid, Valid }
import io.taig.bsts.implicits._
import shapeless.test.illTyped

class RawTest extends Suite {
    it should "be available for Errors" in {
        rule.required.validate( "" ) match {
            case Valid( _ )       ⇒ fail()
            case Invalid( error ) ⇒ error.raw shouldBe ( "required", Nil )
        }
    }

    it should "be available for Terms" in {
        rule.required.validate( "" ).raw shouldBe Invalid( ( "required", List.empty[Any] ) )
    }

    it should "be available for Policies" in {
        ( rule.required ~> mutation.parse ).validate( "asdf" ).raw shouldBe
            Invalid( NonEmptyList( ( "parse", List.empty[Any] ) ) )

        ( transformation.trim ~> rule.required ).validate( "  " ).raw shouldBe
            Invalid( NonEmptyList( ( "required", List.empty[Any] ) ) )
    }

    it should "not be available for Transformations" in {
        illTyped {
            "transformation.trim.validate( \"asdf\" ).raw"
        }
    }
}