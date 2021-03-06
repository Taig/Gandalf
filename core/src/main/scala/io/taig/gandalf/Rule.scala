package io.taig.gandalf

import cats.data.Validated.{ Invalid, Valid }
import shapeless._

abstract class Rule[N <: String, T, A <: HList](
        implicit
        w: Witness.Aux[N]
) extends Term[N, T, T, A] {
    override type V = Rule[N, T, A] :: HNil

    override type E = Error[N, A]

    override def validations = this :: HNil
}

object Rule {
    def apply[T]( name: String ): Builder1[name.type, T] = new Builder1()( Witness.mkWitness( name ) )

    class Builder1[N <: String, T]( implicit w: Witness.Aux[N] ) {
        def apply( f: T ⇒ Boolean ): Rule[N, T, HNil] with Chain1[N, T] = new Rule[N, T, HNil] with Chain1[N, T] {
            override def validate( input: T ) = f( input ) match {
                case true  ⇒ Valid( input )
                case false ⇒ Invalid( Error( HNil ) )
            }

            override def apply[A <: HList]( g: T ⇒ A ): Rule[N, T, A] = new Rule[N, T, A] {
                override def validate( input: T ) = f( input ) match {
                    case true  ⇒ Valid( input )
                    case false ⇒ Invalid( Error( g( input ) ) )
                }
            }
        }
    }

    trait Chain1[N <: String, T] {
        def apply[A <: HList]( f: T ⇒ A ): Rule[N, T, A]
    }

    def apply[T, U]( name: String ): Builder2[name.type, T, U] = new Builder2()( Witness.mkWitness( name ) )

    class Builder2[N <: String, T, U]( implicit w: Witness.Aux[N] ) {
        def apply( g: T ⇒ U )( f: U ⇒ Boolean ): Rule[N, T, HNil] with Chain2[N, T, U] = {
            new Rule[N, T, HNil] with Chain2[N, T, U] {
                override def validate( input: T ) = f( g( input ) ) match {
                    case true  ⇒ Valid( input )
                    case false ⇒ Invalid( Error( HNil ) )
                }

                override def apply[A <: HList]( h: ( T, U ) ⇒ A ): Rule[N, T, A] = new Rule[N, T, A] {
                    override def validate( input: T ) = {
                        val transformed = g( input )

                        f( transformed ) match {
                            case true  ⇒ Valid( input )
                            case false ⇒ Invalid( Error( h( input, transformed ) ) )
                        }
                    }
                }
            }
        }
    }

    trait Chain2[N <: String, T, U] {
        def apply[A <: HList]( f: ( T, U ) ⇒ A ): Rule[N, T, A]
    }
}