package io.taig.gandalf.predef.test

import cats.data.Validated._
import io.taig.gandalf.core.syntax.all._
import io.taig.gandalf.core.test.Suite
import io.taig.gandalf.predef.string._
import io.taig.gandalf.predef.string.{ empty ⇒ isEmpty }

class StringTest extends Suite {
    "empty" should "check if input is empty" in {
        isEmpty.validate( "" ) shouldBe valid( "" )
        isEmpty.validate( "foo" ) shouldBe invalidNel( "empty" )
        isEmpty.validate( " " ) shouldBe invalidNel( "empty" )
    }

    "ltrim" should "remove whitespace at the beginning" in {
        ltrim.validate( "foo" ) shouldBe valid( "foo" )
        ltrim.validate( "  foo" ) shouldBe valid( "foo" )
        ltrim.validate( "foo   " ) shouldBe valid( "foo   " )
        ltrim.validate( "  foo   " ) shouldBe valid( "foo   " )
        ltrim.validate( "     " ) shouldBe valid( "" )
    }

    "matches" should "check if a given regex pattern matches" in {
        matches( ".*foo.*" ).validate( "foo" ) shouldBe valid( "foo" )
        matches( ".*foo.*" ).validate( "foobar" ) shouldBe valid( "foobar" )
        matches( ".*foo.*" ).validate( "bar" ) shouldBe invalidNel( "matches" )
    }

    "required" should "remove all whitespace and check if input is empty" in {
        required.validate( "foo" ) shouldBe valid( "foo" )
        required.validate( "  foo" ) shouldBe valid( "foo" )
        required.validate( "foo   " ) shouldBe valid( "foo" )
        required.validate( "  foo   " ) shouldBe valid( "foo" )
        required.validate( "     " ) shouldBe invalidNel( "not(empty)" )
    }

    "rtrim" should "remove whitespace at the end" in {
        rtrim.validate( "foo" ) shouldBe valid( "foo" )
        rtrim.validate( "  foo" ) shouldBe valid( "  foo" )
        rtrim.validate( "foo   " ) shouldBe valid( "foo" )
        rtrim.validate( "  foo   " ) shouldBe valid( "  foo" )
        rtrim.validate( "     " ) shouldBe valid( "" )
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