package io.taig.gandalf.tests

import cats.data.Validated.invalidNel
import io.taig.gandalf.Error
import io.taig.gandalf.predef.{ Required, Trim }
import io.taig.gandalf.syntax.all._

class MessagesTest extends Suite {
    it should "be possible to provide custom messages for expressions" in {
        type MyRequired = Trim ~> Required
        val myRequired = Trim.trim ~> Required.required
        myRequired.validate( "yolo" )
        //        implicit val error = Error.instance[MyRequired]( "This string is empty" )
        //        tryLift[MyRequired]( "   " ) shouldBe invalidNel( "This string is empty" )
    }
}