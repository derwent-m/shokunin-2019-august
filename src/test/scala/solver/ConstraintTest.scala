package solver

import org.scalatest.{Matchers, FunSuite}

class ConstraintSatisfactionTest extends FunSuite with Matchers {
  test("one variable, satisfied") {
    val constraint = new Constraint(Vector(1, 2))  // 1 * x < 2
    val assignment = Vector(1) // x == 1
    constraint.isSatisfiedBy(assignment) should be(true)
  }

  test("one variable, not satisfied") {
    val constraint = new Constraint(Vector(1, 2))  // 1 * x < 2
    val assignment = Vector(3) // x == 3
    constraint.isSatisfiedBy(assignment) should be(false)
  }

  test("two variables, satisfied") {
    val constraint = new Constraint(Vector(1, 1, 3)) // x + y < 2
    val assignment = Vector(1, 1) // x == 1, y == 1
    constraint.isSatisfiedBy(assignment) should be(true)
  }

  test("two variables, not satisfied") {
    val constraint = new Constraint(Vector(1, 1, 2)) // x + y < 2
    val assignment = Vector(1, 3) // x == 1, y == 3
    constraint.isSatisfiedBy(assignment) should be(false)
  }

  test("all zeros") {
    val constraint = new Constraint(Vector(0, 0, 0)) // 0 < 0
    val assignment = Vector(1, 2) // x == 1, y == 2
    constraint.isSatisfiedBy(assignment) should be(false)
  }
}

class PartiallySolveInequalityTest extends FunSuite with Matchers {
  test("two variables, solve first") {
    val constraint = new Constraint(Vector(1, 1, 2)) // x + y < 2
    // solve for x == 1
    constraint.partiallySolve(0, 1).vector should be(
      Vector(1, 1) // y < 1
    )
  }
  test("two variables, solve second") {
    val constraint = new Constraint(Vector(2, -3, 5)) // 2x - 3y < 5
    // solve for y == 3
    constraint.partiallySolve(1, 3).vector should be(
      Vector(2, 14) // 2x < 14
    )
  }
}
