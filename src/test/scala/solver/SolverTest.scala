package solver

import org.scalatest.{Matchers, FunSuite}

/** @version 1.0.0 */
class SimplifyInequalitiesTest extends FunSuite with Matchers {
  test("one variable, redundant rules") {
    pending
    val matrix = Vector(
      SingleConstraint(Vector(1, 2)), // 1 * x < 2
      SingleConstraint(Vector(1, 1)) // 1 * x < 1
    )
    Solver.simplifyInequalities[Int](matrix) should be(
      Vector(
        SingleConstraint(Vector(1, 1)) // 1 * x < 1
      )
    )
  }

  test("one variable, redundant rules, non-normalised") {
    pending
    val matrix = Vector(
      SingleConstraint(Vector(1, 2)), // 1 * x < 2
      SingleConstraint(Vector(2, 2)) // 2 * x < 2 => 1 * x < 1
    )
    Solver.simplifyInequalities[Int](matrix) should be(
      Vector(
        SingleConstraint(Vector(1, 1)) // 1 * x < 1
      )
    )
  }

  /**
    * These normals are in opposite directions, so shouldn't cancel out
    */
  test("one variable, redundant rules, planes in opposite directions, convex") {
    pending
    val matrix = Vector(
      SingleConstraint(Vector(-1, -1)), // x > 1 => -x < -1
      SingleConstraint(Vector(1, 3)) // x < 3
    )
    Solver.simplifyInequalities[Int](matrix) should be(
      Vector(
        SingleConstraint(Vector(-1, -1)), // x > 1 => -x < -1
        SingleConstraint(Vector(1, 3)) // x < 3
      )
    )
  }
}

class BruteForceInequalitiesTest extends FunSuite with Matchers {
  test("one variable, one solution") {
    val matrix = Vector(
      SingleConstraint(Vector(-1, -1)), // x > 1 => -x < -1
      SingleConstraint(Vector(1, 3)) // x < 3
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
      SingleConstraint(Vector(-1, -1)), // x > 1 => -x < -1
      SingleConstraint(Vector(1, 4)) // x < 3
    )
    val variables = Vector(
      0 to 10 // x can be anywhere from 0 to 10
    )
    Solver.bruteForceInequalities(matrix, variables).toSet should be(
      Vector(Vector(2), Vector(3)).toSet // i.e. [x == 2, x == 3]
    )
  }

  test("one variable, no solution") {
    val matrix = Vector(
      SingleConstraint(Vector(1, 1)), // x < 1
      SingleConstraint(Vector(-1, -3)) // x > 3 => -x < -3
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
      SingleConstraint(Vector(1, 1, 4)), // x + y < 4
      SingleConstraint(Vector(-1, -1, -2)), // x + y > 2 => -x -y < -2
      SingleConstraint(Vector(-1, 0, 0)), // x > 0 => -x < 0
      SingleConstraint(Vector(1, 0, 2)) // x < 2
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
      SingleConstraint(Vector(1, 1, 4)), // x + y < 4
      SingleConstraint(Vector(-1, -1, -2)), // x + y > 2 => -x -y < -2
      SingleConstraint(Vector(-1, 0, 0)), // x > 0 => -x < 0
      SingleConstraint(Vector(1, 2)) // x < 2
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
      SingleConstraint(Vector(1, 1, 4)), // x + y < 4
      SingleConstraint(Vector(-1, -1, -2)) // x + y > 2 => -x -y < -2
    )
    val variables = Vector(
      0 to 10, // x can be anywhere from 0 to 10
      0 to 10 // y can be anywhere from 0 to 10
    )
    Solver.bruteForceInequalities(matrix, variables).toSet should be(
      Vector(
        Vector(0, 3),
        Vector(1, 2),
        Vector(2, 1),
        Vector(3, 0)
      ).toSet
    )
  }

  test("two variable, one solution") {
    // x == 1, y == 2
    val matrix = Vector(
      SingleConstraint(Vector(1, 1, 4)), // x + y < 4
      SingleConstraint(Vector(-1, -1, -2)), // x + y > 2 => -x -y < -2
      SingleConstraint(Vector(-1, 0, 0)), // x > 0 => -x < 0
      SingleConstraint(Vector(1, 0, 2)) // x < 2
    )
    val variables = Vector(
      0 to 10, // x can be anywhere from 0 to 10
      0 to 10 // y can be anywhere from 0 to 10
    )
    Solver.bruteForceInequalities(matrix, variables) should be(
      Vector(Vector(1, 2))
    )
  }

  test("values are unique") {
    // x == 1, y == 2
    val matrix = Vector(
      SingleConstraint(Vector(1, 1, 3)), // x + y < 3
      SingleConstraint(Vector(-1, -1, -1)) // x + y > 1 => -x -y < -1
    )
    val variables = Vector(
      0 to 10, // x can be anywhere from 0 to 10
      0 to 10 // y can be anywhere from 0 to 10
    )
    Solver.bruteForceInequalities(matrix, variables).toSet should be(
      Vector(
        Vector(0, 2),
        Vector(2, 0)
      ).toSet
    )
  }

  test("the whole gang") {
    val n = 5 // number of people
    val matrix = Vector(
      SingleConstraint(Vector(-1, 0, 0, 0, 0, -1)), // Je > 1 => -Je < -1
      SingleConstraint(Vector(0, 1, 0, 0, 0, n)), // Ev < n
      SingleConstraint(Vector(0, 0, -1, 0, 0, -1)), // Jo > 1 => -Jo < -1
      SingleConstraint(Vector(0, 0, 1, 0, 0, n)), // Jo < n
      SingleConstraint(Vector(0, 1, 0, -1, 0, 0)) // Sa > Ev => Ev - Sa < 0
    )
    // Ranks can be anywhere from 1 to n
    val variables = (1 to n).map(_ => 1 to n).toVector
    Solver.bruteForceInequalities(matrix, variables).toSet should be(
      Vector(
        Vector(2, 1, 3, 4, 5),
        Vector(2, 1, 3, 5, 4),
        Vector(2, 1, 4, 3, 5),
        Vector(2, 1, 4, 5, 3),
        Vector(2, 3, 4, 5, 1),
        Vector(2, 4, 3, 5, 1),
        Vector(3, 1, 2, 4, 5),
        Vector(3, 1, 2, 5, 4),
        Vector(3, 1, 4, 2, 5),
        Vector(3, 1, 4, 5, 2),
        Vector(3, 2, 4, 5, 1),
        Vector(3, 4, 2, 5, 1),
        Vector(4, 1, 2, 3, 5),
        Vector(4, 1, 2, 5, 3),
        Vector(4, 1, 3, 2, 5),
        Vector(4, 1, 3, 5, 2),
        Vector(4, 2, 3, 5, 1),
        Vector(4, 3, 2, 5, 1),
        Vector(5, 1, 2, 3, 4),
        Vector(5, 1, 2, 4, 3),
        Vector(5, 1, 3, 2, 4),
        Vector(5, 1, 3, 4, 2),
        Vector(5, 1, 4, 2, 3),
        Vector(5, 1, 4, 3, 2),
        Vector(5, 2, 3, 4, 1),
        Vector(5, 2, 4, 3, 1),
        Vector(5, 3, 2, 4, 1)
      ).toSet
    )
  }
}
