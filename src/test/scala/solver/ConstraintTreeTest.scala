package solver

import org.scalatest.{Matchers, FunSuite}

/** @version 1.0.0 */
class ConstraintTreeTest extends FunSuite with Matchers {
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
