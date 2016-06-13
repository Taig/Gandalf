package io.taig.gandalf

import cats.data.NonEmptyList
import io.taig.gandalf.data.{ Action, Operation }
import shapeless.HNil
import shapeless.record._
import shapeless.syntax.singleton._

trait Error[-A <: Arguments] {
    def error( arguments: A#Arguments ): NonEmptyList[String]
}

object Error {
    /**
     * Error representation that only provides the input value
     *
     * Intended to be primarily used with Rules.
     */
    type Input[A <: Action with Arguments] = Record.`"input" -> A#Input`.T

    /**
     * Error representation that only provides the input value
     *
     * Intended to be primarily used with Rules.
     */
    def input[A <: Action with Arguments]( input: A#Input ): Input[A] = "input" ->> input :: HNil

    /**
     * Error representation that provides the input and an expected value
     *
     * Intended to be primarily used with Mutations.
     */
    type Expectation[A <: Action with Arguments] = Record.`"input" -> A#Input, "expected" -> A#Output`.T

    /**
     * Error representation that provides the input and an expected value
     *
     * Intended to be primarily used with Mutations.
     */
    def expectation[A <: Action with Arguments]( input: A#Input, expected: A#Output ): Expectation[A] = {
        "input" ->> input :: "expected" ->> expected :: HNil
    }

    /**
     * Error representation that provides the input and accumulated errors
     *
     * Intended to be primarily used with Operations.
     */
    type Forward[A <: Action with Arguments] = Record.`"input" -> A#Input, "errors" -> NonEmptyList[String]`.T

    /**
     * Error representation that provides the input and accumulated errors
     *
     * Intended to be primarily used with Operations.
     */
    def forward[A <: Action with Arguments]( input: A#Input, errors: NonEmptyList[String] ): Forward[A] = {
        "input" ->> input :: "errors" ->> errors :: HNil
    }

    implicit val errorOperation: Error[Operation] = new Error[Operation] {
        override def error( arguments: Operation#Arguments ) = arguments( "errors" )
    }

    @inline
    def apply[A <: Arguments: Error]: Error[A] = implicitly[Error[A]]

    def instance[A <: Arguments]( message: String ): Error[A] = new Error[A] {
        override def error( arguments: A#Arguments ) = NonEmptyList( message )
    }

    def instance[A <: Arguments]( f: A#Arguments ⇒ String ): Error[A] = new Error[A] {
        override def error( arguments: A#Arguments ) = NonEmptyList( f( arguments ) )
    }
}