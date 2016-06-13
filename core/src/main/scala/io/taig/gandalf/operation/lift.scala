package io.taig.gandalf.operation

import io.taig.gandalf._
import io.taig.gandalf.data.{ Action, Obeys }
import io.taig.gandalf.internal.Macro
import io.taig.gandalf.syntax.aliases._

import scala.language.experimental.macros
import scala.language.implicitConversions

trait lift {
    implicit def valueToLifted[I, O, A <: Action.Aux[I, O]]( value: I )(
        implicit
        v: Validation[O, A]
    ): I Obeys A = macro Macro.lift[I, O, A]

    implicit def liftedToValue[V <: Action]( lifted: V#Input Obeys V ): V#Output = lifted.value

    //    def lift[V <: Action]: LiftHelper[V] = new LiftHelper[V]
    //
    //    class LiftHelper[V <: Action] {
    //        def apply[I <: V#Input]( value: I )(
    //            implicit
    //            e:  V,
    //            ts: TypeShow[V]
    //        ): I Obeys V = macro Macro.lift[I, V]
    //    }

    def tryLift[A <: Action]( value: A#Input )( implicit v: Validation[_, A] ): v.Output = v.validate( value )
}