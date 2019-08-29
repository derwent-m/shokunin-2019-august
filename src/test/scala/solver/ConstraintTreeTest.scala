package solver

import org.scalatest.{Matchers, FunSuite}

/** @version 1.0.0 */
class ConstraintTreeSatisfiedTest extends FunSuite with Matchers {
  test("one variable, isSatisfiedBy, leaf") {
    val tree = ConstraintTree[Int](
      Leaf(SingleConstraint(Vector(-1, 0)))
    )

    tree.isSatisfiedBy(Vector(1)) should be(true)
  }

  test("one variable, isSatisfiedBy, node, and") {
    val tree = ConstraintTree[Int](Node(
      Logic.and,
      Leaf(SingleConstraint(Vector(1, 2))),
      Leaf(SingleConstraint(Vector(-1, 0)))
    ))

    tree.isSatisfiedBy(Vector(1)) should be(true)
  }

  test("one variable, isSatisfiedBy, node, or") {
    val tree = ConstraintTree[Int](Node(
      Logic.or,
      Leaf(SingleConstraint(Vector(1, 2))),
      Leaf(SingleConstraint(Vector(-1, -4)))
    ))

    tree.isSatisfiedBy(Vector(5)) should be(true)
    tree.isSatisfiedBy(Vector(1)) should be(true)
    tree.isSatisfiedBy(Vector(2)) should be(false)
  }

  test("multiple variable, isSatisfiedBy, and") {
    val n = 5 // number of people
    val tree = ConstraintTree[Int](Node(
      Logic.and,
      Leaf(SingleConstraint(Vector(-1, 0, 0, 0, 0, -1))), // Je > 1 => -Je < -1
      Node(
        Logic.and,
        Leaf(SingleConstraint(Vector(0, 1, 0, 0, 0, n))), // Ev < n
        Node(
          Logic.and,
          Leaf(SingleConstraint(Vector(0, 0, -1, 0, 0, -1))), // Jo > 1 => -Jo < -1
          Node(
            Logic.and,
            Leaf(SingleConstraint(Vector(0, 0, 1, 0, 0, n))), // Jo < n
            Leaf(SingleConstraint(Vector(0, 1, 0, -1, 0, 0))) // Sa > Ev => Ev - Sa < 0
          )
        )
      )
    ))

    tree.isSatisfiedBy(Vector(2, 1, 3, 4, 5)) should be(true)
    tree.isSatisfiedBy(Vector(1, 2, 3, 4, 5)) should be(false)
  }
}

class ConstraintTreePartiallySolveTest extends FunSuite with Matchers {
  test("two variables, partiallySolve, leaf") {
    val tree = ConstraintTree[Int](
      Leaf(SingleConstraint(Vector(1, 1, 2))) // x + y < 2
    )

    // solve for x == 1
    val solution = tree.partiallySolve(0, 1).t.leafValue getOrElse(SingleConstraint(Vector.empty[Int]))
    solution.vector should be(
      Vector(1, 1) // y < 1
    )
  }

  test("two variables, partiallySolve, node") {
    val tree = ConstraintTree[Int](
      Node(
        Logic.and,
        Leaf(SingleConstraint(Vector(1, 1, 2))), // x + y < 2
        Leaf(SingleConstraint(Vector(2, -3, 5))) // 2x - 3y < 5
      )
    )

    // solve for x == 3
    val solution = tree.partiallySolve(1, 3).t
    solution.left.get.leafValue.get.vector should be(
      Vector(1, -1) // y < 1
    )
    solution.right.get.leafValue.get.vector should be (
      Vector(2, 14)
    )
  }
}


class ConstraintTreeAndOr extends FunSuite with Matchers {
  val leafLeft = ConstraintTree[Int](
    Leaf(SingleConstraint(Vector(-1, 0))) // x > 0
  )
  val leafRight = ConstraintTree[Int](
    Leaf(SingleConstraint(Vector(1, 2))) // x < 2
  )
  test("leaf and leaf") {
    val tree = leafLeft.and(leafRight.asInstanceOf[leafLeft.T])
    tree.t.left.get.leafValue.get.vector should be (
      Vector(-1, 0)
    )
    tree.t.right.get.leafValue.get.vector should be (
      Vector(1, 2)
    )
    tree.t.nodeValue.get should be (
      Logic.and
    )
  }

  test("leaf or leaf") {
    val tree = leafLeft.or(leafRight.asInstanceOf[leafLeft.T])
    tree.t.left.get.leafValue.get.vector should be (
      Vector(-1, 0)
    )
    tree.t.right.get.leafValue.get.vector should be (
      Vector(1, 2)
    )
    tree.t.nodeValue.get should be (
      Logic.or
    )
  }
}
