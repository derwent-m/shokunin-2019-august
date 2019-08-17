package solver

import org.scalatest.{Matchers, FunSuite}

/** @version 1.0.0 */
class SimplifyInequalitiesTest extends FunSuite with Matchers {
  test("one variable, redundant rules") {
    pending
    val matrix = Vector(
      Vector(1, 2), // 1 * x < 0
      Vector(1, 1) // 1 * x < 1
    )
    Solver.simplifyInequalities[Int](matrix) should be( Vector(
      Vector( 1, 1 ) // 1 * x < 1
    ))
  }

  test("one variable, redundant rules, non-normalised") {
    pending
    val matrix = Vector(
      Vector(1, 2), // 1 * x < 2
      Vector(2, 2) // 2 * x < 2 => 1 * x < 1
    )
    Solver.simplifyInequalities[Int](matrix) should be( Vector(
      Vector( 1, 1 ) // 1 * x < 1
    ))
  }

  /**
   * These normals are in opposite directions, so shouldn't cancel out
   */
  test("one variable, redundant rules, planes in opposite directions, convex") {
    pending
    val matrix = Vector(
      Vector(-1, -1), // x > 1 => -x < -1
      Vector(1, 3) // x < 3
    )
    Solver.simplifyInequalities[Int](matrix) should be( Vector(
      Vector(-1, -1), // x > 1 => -x < -1
      Vector(1, 3) // x < 3
    ))
  }
}

class BruteForceInequalitiesTest extends FunSuite with Matchers {
  test("one variable, one solution") {
    val matrix = Vector(
      Vector(-1, -1), // x > 1 => -x < -1
      Vector(1, 3) // x < 3
    )
    val variables = Vector(
      0 to 10 // x can be anywhere from 0 to 10
    )
    Solver.bruteForceInequalities(matrix, variables) should be(
      Vector(Vector(2)) // i.e. [x == 2,]
    )
  }

  test("one variable, two solution") {
    val matrix = Vector(
      Vector(-1, -1), // x > 1 => -x < -1
      Vector(1, 4) // x < 3
    )
    val variables = Vector(
      0 to 10 // x can be anywhere from 0 to 10
    )
    Solver.bruteForceInequalities(matrix, variables) should be(
      Vector( Vector(2), Vector(3) ) // i.e. [x == 2, x == 3]
    )
  }
}
