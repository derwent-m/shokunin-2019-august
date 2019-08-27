package solver

object Logic {
  val and : Function2[Boolean, Boolean, Boolean] = {(a, b)=> a && b}
  val or : Function2[Boolean, Boolean, Boolean] = {(a, b)=> a || b}
}
