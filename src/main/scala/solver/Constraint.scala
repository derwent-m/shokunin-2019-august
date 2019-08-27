package solver

trait Constraint[A, T <: Constraint[A, T]] {
  def isSatisfiedBy(assignment: Vector[A]): Boolean
  def partiallySolve(index: Int, value: A): T
}

/**
  * A single constraint which determines the values that a set of
  * variables can have. Constraints are strict integer linear inequalities
  */
case class SingleConstraint[A](vector: Vector[A])(implicit num: Numeric[A])
    extends Constraint[A, SingleConstraint[A]] {
  def length: Int = vector.length

  /**
    * Determines if this constraint is satisfied by a particular
    * choice of values
    */
  def isSatisfiedBy(assignment: Vector[A]): Boolean = {
    // Left side of inequality
    val left = (vector zip assignment)
      .map({ case (coeff, value) => num.times(coeff, value) })
      .sum
    // Right side of inequality
    val right = vector.last
    num.lt(left, right)
  }

  /**
    * Substitute the value of the variable at [[index]] with [[value]]
    * and re-state the constraint without this variable
    */
  def partiallySolve(index: Int, value: A): this.type = {
    copy(
      vector
        .patch(index, Vector(), 1)
        .updated(
          length - 2,
          num.minus(vector.last, num.times(vector(index), value))
        )
    ).asInstanceOf[this.type]
  }
}
