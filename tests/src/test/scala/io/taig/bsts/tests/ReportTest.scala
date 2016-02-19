package io.taig.bsts.tests

import cats.data.NonEmptyList
import cats.data.Validated.{ Invalid, Valid }
import io.taig.bsts._
import io.taig.bsts.report.ReportableTerm
import io.taig.bsts.report.syntax.report._
import io.taig.bsts.syntax.dsl._
import shapeless.HNil
import shapeless.test.illTyped

class ReportTest extends Suite {
    it should "be available for Errors" in {
        import report._

        rule.required.validate( "" ) match {
            case Valid( _ )       ⇒ fail()
            case Invalid( error ) ⇒ error.report shouldBe "Pflichtfeld"
        }

        mutation.parse.validate( "foo" ) match {
            case Valid( _ )       ⇒ fail()
            case Invalid( error ) ⇒ error.report shouldBe "Keine gültige Zahl"
        }
    }

    it should "be available for Terms" in {
        import report._

        rule.required.validate( "" ).report shouldBe Invalid( "Pflichtfeld" )

        mutation.parse.validate( "foo" ).report shouldBe Invalid( "Keine gültige Zahl" )
    }

    it should "be available for Policies" in {
        import report._

        Policy( rule.required :: HNil ).validate( "" ).report shouldBe Invalid( NonEmptyList( "Pflichtfeld" ) )
        Policy( rule.min( 6 ) :: HNil ).validate( "" ).report shouldBe Invalid( NonEmptyList( "Mindestens 6 Zeichen" ) )

        ( rule.required & rule.min( 6 ) ).validate( "foo" ).report shouldBe
            Invalid( NonEmptyList( "Mindestens 6 Zeichen" ) )

        ( transformation.trim ~> rule.min( 6 ) ).validate( "foo     " ).report shouldBe
            Invalid( NonEmptyList( "Mindestens 6 Zeichen" ) )
    }

    it should "not be available for Transformations" in {
        illTyped {
            "transformation.trim.validate( \"asdf\" ).report"
        }
    }

    it should "be possible to attach reports to Terms" in {
        rule.required.as( _ ⇒ "yolo" ) shouldBe a[ReportableTerm[_, _, _, _]]
    }

    it should "be possible to report a ReportableError" in {
        rule.required.as( _ ⇒ "yolo" ).validate( "" ) match {
            case Valid( _ )       ⇒ fail()
            case Invalid( error ) ⇒ error.report shouldBe "yolo"
        }
    }

    it should "be possible to report a Validated[ReportableError]" in {
        rule.required.as( _ ⇒ "yolo" ).validate( "" ).report shouldBe Invalid( "yolo" )
    }
}