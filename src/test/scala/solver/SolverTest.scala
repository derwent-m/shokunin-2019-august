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

class ConstraintSatisfactionTest extends FunSuite with Matchers {
  test("one variable, satisfied") {
    val inequality = Vector(1, 2) // 1 * x < 2
    val values = Vector(1) // x == 1
    Solver.testConstraintSatisfaction(inequality, values) should be(true)
  }

  test("one variable, not satisfied") {
    val inequality = Vector(1, 2) // 1 * x < 2
    val values = Vector(3) // x == 3
    Solver.testConstraintSatisfaction(inequality, values) should be(false)
  }

  test("two variables, satisfied") {
    val inequality = Vector(1, 1, 3) // x + y < 2
    val values = Vector(1, 1) // x == 1, y == 1
    Solver.testConstraintSatisfaction(inequality, values) should be(true)
  }

  test("two variables, not satisfied") {
    val inequality = Vector(1, 1, 2) // x + y < 2
    val values = Vector(1, 3) // x == 1, y == 3
    Solver.testConstraintSatisfaction(inequality, values) should be(false)
  }

  test("all zeros") {
    val inequality = Vector(0, 0, 0) // 0 < 0
    val values = Vector(1, 2) // x == 1, y == 2
    Solver.testConstraintSatisfaction(inequality, values) should be(false)
  }
}

class PartiallySolveInequalityTest extends FunSuite with Matchers {
  test("two variables, solve first") {
    val inequality = Vector( 1, 1, 2 ) // x + y < 2
    // solve for x == 1
    Solver.partiallySolveInequality(inequality, 0, 1) should be (
      Vector(1, 1) // y < 1
    )
  }
  test("two variables, solve second") {
    val inequality = Vector( 2, -3, 5 ) // 2x - 3y < 5
    // solve for y == 3
    Solver.partiallySolveInequality(inequality, 1, 3) should be (
      Vector(2, 14) // 2x < 14
    )
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

  test("one variable, two solutions") {
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

  test("one variable, no solution") {
    val matrix = Vector(
      Vector(1, 1), // x < 1
      Vector(-1, -3) // x > 3 => -x < -3
    )
    val variables = Vector(
      0 to 10 // x can be anywhere from 0 to 10
    )
    Solver.bruteForceInequalities(matrix, variables) should be(
      Vector.empty[Vector[Int]] // i.e. []
    )
  }

  test("less variables than constraints") {
    // x == 1, y == 2
    val matrix = Vector(
      Vector(1, 1, 4), // x + y < 4
      Vector(-1, -1, -2), // x + y > 2 => -x -y < -2
      Vector(-1, 0, 0), // x > 0 => -x < 0
      Vector(1, 0, 2) // x < 2
    )
    val variables = Vector(
      0 to 10 // x can be anywhere from 0 to 10
    )
    assertThrows[IllegalArgumentException] {
      Solver.bruteForceInequalities(matrix, variables)
    }
  }

  test("uneven constraints") {
    // x == 1, y == 2
    val matrix = Vector(
      Vector(1, 1, 4), // x + y < 4
      Vector(-1, -1, -2), // x + y > 2 => -x -y < -2
      Vector(-1, 0, 0), // x > 0 => -x < 0
      Vector(1, 2) // x < 2
    )
    val variables = Vector(
      0 to 10, // x can be anywhere from 0 to 10
      1 to 5
    )
    assertThrows[IllegalArgumentException] {
      Solver.bruteForceInequalities(matrix, variables)
    }
  }

  test("two variable, multiple solution") {
    // x == 1, y == 2
    val matrix = Vector(
      Vector(1, 1, 4), // x + y < 4
      Vector(-1, -1, -2), // x + y > 2 => -x -y < -2
    )
    val variables = Vector(
      0 to 10, // x can be anywhere from 0 to 10
      0 to 10, // y can be anywhere from 0 to 10
    )
    Solver.bruteForceInequalities(matrix, variables) should be(
      Vector(
        Vector(0, 3),
        Vector(1, 2),
        Vector(2, 1),
        Vector(3, 0),
      )
    )
  }

  test("two variable, one solution") {
    // x == 1, y == 2
    val matrix = Vector(
      Vector(1, 1, 4), // x + y < 4
      Vector(-1, -1, -2), // x + y > 2 => -x -y < -2
      Vector(-1, 0, 0), // x > 0 => -x < 0
      Vector(1, 0, 2) // x < 2
    )
    val variables = Vector(
      0 to 10, // x can be anywhere from 0 to 10
      0 to 10, // y can be anywhere from 0 to 10
    )
    Solver.bruteForceInequalities(matrix, variables) should be(
      Vector( Vector(1, 2) )
    )
  }
}
