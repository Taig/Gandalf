package io.taig.gandalf.predef.test

import cats.data.Validated._
import io.taig.gandalf.predef.string.{ regex ⇒ regexxx, _ }
import io.taig.gandalf.syntax.all._
import io.taig.gandalf.test.Suite

class StringTest extends Suite {
    "nonEmpty" should "check if input is empty" in {
        nonEmpty.validate( "foo" ) shouldBe valid( "foo" )
        nonEmpty.validate( " " ) shouldBe valid( " " )
        nonEmpty.validate( "" ) shouldBe invalidNel( "nonEmpty" )
    }

    "regex" should "check if a given pattern equal" in {
        regexxx( ".*foo.*" ).validate( "foo" ) shouldBe valid( "foo" )
        regexxx( ".*foo.*" ).validate( "foobar" ) shouldBe valid( "foobar" )
        regexxx( ".*foo.*" ).validate( "bar" ) shouldBe invalidNel( "regex" )
    }

    "required" should "remove all whitespace and check if input is empty" in {
        required.validate( "foo" ) shouldBe valid( "foo" )
        required.validate( "  foo" ) shouldBe valid( "foo" )
        required.validate( "foo   " ) shouldBe valid( "foo" )
        required.validate( "  foo   " ) shouldBe valid( "foo" )
        required.validate( "     " ) shouldBe invalidNel( "nonEmpty" )
    }

    "toLower" should "replace all uppercase letters" in {
        toLower.validate( "foo" ) shouldBe valid( "foo" )
        toLower.validate( "Foo" ) shouldBe valid( "foo" )
        toLower.validate( "FooBar" ) shouldBe valid( "foobar" )
    }

    "toUpper" should "replace all lowercase letters" in {
        toUpper.validate( "FOO" ) shouldBe valid( "FOO" )
        toUpper.validate( "Foo" ) shouldBe valid( "FOO" )
        toUpper.validate( "FooBar" ) shouldBe valid( "FOOBAR" )
    }

    "trim" should "remove all whitespace" in {
        trim.validate( "foo" ) shouldBe valid( "foo" )
        trim.validate( "  foo" ) shouldBe valid( "foo" )
        trim.validate( "foo   " ) shouldBe valid( "foo" )
        trim.validate( "  foo   " ) shouldBe valid( "foo" )
        trim.validate( "     " ) shouldBe valid( "" )
    }
}