import solver._

object Main extends App  {
  val people = Vector(
    "Je", "Ev", "Jo", "Sa", "Ma"
  )
  val n = people.length // number of people
  val tree = ConstraintTree[Int](
    // Jessie is not the best developer
    Leaf(SingleConstraint(Vector(-1, 0, 0, 0, 0, -1))) // Je > 1 => -Je < -1
  ).and(ConstraintTree[Int](
    // Evan is not the worst developer
    Leaf(SingleConstraint(Vector(0, 1, 0, 0, 0, n))) // Ev < n
  )).and(ConstraintTree[Int](
    // John is not the best developer or the worst developer
    Leaf(SingleConstraint(Vector(0, 0, -1, 0, 0, -1))) // Jo > 1 => -Jo < -1
  )).and(ConstraintTree[Int](
    Leaf(SingleConstraint(Vector(0, 0, 1, 0, 0, n))) // Jo < n
  )).and(ConstraintTree[Int](
    // Sarah is a better developer than Evan
    Leaf(SingleConstraint(Vector(0, -1, 0, 1, 0, 0))) // Sa > Ev => Ev - Sa < 0
  )).and(
    // Matt is not directly below or above John as a developer
    ConstraintTree[Int](
      Leaf(SingleConstraint(Vector(0, 0, -1, 0, 1, -1))) // Ma < Jo - 1 => Ma - Jo < -1
    ).or(ConstraintTree[Int](
      Leaf(SingleConstraint(Vector(0, 0, 1, 0, -1, -1))) // Ma > Jo + 1 => Jo - Ma < -1
    ))
  ).and(
    // John is not directly below or above Evan as a developer
    ConstraintTree[Int](
      Leaf(SingleConstraint(Vector(0, -1, 1, 0, 0, -1))) // Jo < Ev - 1 => Jo - Ev < -1
    ).or(ConstraintTree[Int](
      Leaf(SingleConstraint(Vector(0, 1, -1, 0, 0, -1))) // Jo > Ev + 1 => Ev - Jo < -1
    ))
  )
  // Ranks can be anywhere from 1 to n
  val variables = (1 to n).map(_ => 1 to n).toVector
  val solutions = Solver.bruteForceInequalities( tree, variables )
  println(s"Solutions:")
  for { solution <- solutions } {

    val rankings = for ((ranking, person) <- solution.zipWithIndex) yield Tuple2(ranking, people(person))
    println(rankings.sorted.map(tuple => tuple._2).mkString(", "))
  }
}
