package io.taig.gandalf.predef

import io.taig.gandalf.ops.Parser
import io.taig.gandalf._
import shapeless._
import shapeless.syntax.singleton._

trait string {
    val email = {
        val pattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$".r.pattern

        Rule[String]( "email" )( pattern.matcher( _ ).matches() ) { "value" ->> _ :: HNil }
    }

    def exactly( length: Int ) = Rule[String, Int]( "exactly" )( _.length )( _ == length ) { ( value, actual ) ⇒
        "value" ->> value :: "expected" ->> length :: "actual" ->> actual :: HNil
    }

    val lowercase = Transformation[String, String]( "lowercase" )( _.toLowerCase )

    def max( length: Int ) = Rule[String, Int]( "max" )( _.length )( _ <= length ) { ( value, actual ) ⇒
        "value" ->> value :: "expected" ->> length :: "actual" ->> actual :: HNil
    }

    def min( length: Int ) = Rule[String, Int]( "min" )( _.length )( _ >= length ) { ( value, actual ) ⇒
        "value" ->> value :: "expected" ->> length :: "actual" ->> actual :: HNil
    }

    val removeWhitespace = Transformation[String, String]( "removeWhitespace" )( _.replaceAll( "\\s", "" ) )

    def parse[T: Parser] = Mutation[String, T]( "parse" )( implicitly[Parser[T]].parse )

    val phone = {
        val pattern = "^\\+?0{0,2}[1-9]\\d{3,}$".r.pattern

        Rule[String]( "phone" )( pattern.matcher( _ ).matches() ){ "value" ->> _ :: HNil }
    }

    val required = Rule[String]( "required" )( _.nonEmpty )

    val trim = Transformation[String, String]( "trim" )( _.trim )

    val uppercase = Transformation[String, String]( "uppercase" )( _.toUpperCase )
}

object string extends string