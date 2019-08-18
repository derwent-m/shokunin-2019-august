package solver

/** Provides classes manipulating matrices , representing systems of inequalities
  */
object Solver {

  /** Simplify a set of inequalities in the form of a matrix over type A
    *
    *  @param inequalities a system of inequalities expressed as a matrix over type A.
    *  @return the simplified matrix
    */
  def simplifyInequalities[A](
      inequalities: Vector[Vector[A]]
  ): Vector[Vector[A]] = {
    // TODO: implement this
    inequalities
  }
  // Breakdown:
  // - Normalise constraints by finding factors, but don't invert signs
  // - Sort constraints
  // - Find all equivalent normals by looking at neighbors in sorted constraints
  // - Remove redundant constraints

  /**
    * For each variable, return its' lower and upper bounds based on the constraints matrix
    * Or nil if it is missing either bound.
    *
    * @param inequalities a (simplified) system of inequalities expressed as a matrix over type A.
    * @return the solution widths
    */
  def solutionBounds[A](
      inequalities: Vector[Vector[A]]
  ): Vector[Tuple2[Option[A], Option[A]]] = ???
  //

  /**
    * Determines if constraint expressed as a vector over type A is satisfied by a particular choice of values
    */
  def testConstraintSatisfaction[A: Numeric](
      inequality: Vector[A],
      values: Vector[A]
  ): Boolean = {
    val left = (inequality zip values)
      .map({
        case (coeff, value) => implicitly[Numeric[A]].times(coeff, value)
      })
      .sum
    val right = inequality.last
    implicitly[Numeric[A]].lt(left, right)
  }

  /**
    * Determines if a system of constraints expressed as a vector over type A is satisfied by a particular choice of values
    */
  def testConstraintsSatisfaction[A: Numeric](
      inequalities: Vector[Vector[A]],
      values: Vector[A]
  ): Boolean = {
    inequalities forall { (inequality) =>
      testConstraintSatisfaction(inequality, values)
    }
  }

  /**
    * Partially solves an inequality, rewrites the inequality in terms of this new information
    */
  def partiallySolveInequality[A: Numeric](
      inequality: Vector[A],
      index: Int,
      value: A
  ): Vector[A] = {
    val result = inequality.patch(index, Vector(), 1)
    result.updated(
      result.length - 1,
      implicitly[Numeric[A]].minus(
        result.last,
        implicitly[Numeric[A]].times(inequality(index), value)
      )
    )
  }

  def partiallySolveInequalities[A: Numeric](
      inequalities: Vector[Vector[A]],
      index: Int,
      value: A
  ): Vector[Vector[A]] = {
    inequalities.map(
      (inequality) => partiallySolveInequality(inequality, index, value)
    )
  }

  /**
    * Recursively brute forces all solutions which solve a set of integer linear inequalities
    * for variables that can have any value specified by a range
    * where all values are different.
    *
    * @param inequalities a system of inequalities expressed as a matrix over type A.
    * @param variables a range of values each variable can be for each variable
    * @param usedValues the values which have already been used in the solution
    * @return All possible solutions to these inequalities
    */
  def bruteForceInequalities(
      inequalities: Vector[Vector[Int]],
      variables: Vector[Range],
      usedValues: Set[Int] = Set()
  ): Vector[Vector[Int]] = {
    val inequalitiesSimplified = simplifyInequalities(inequalities)

    val inequalityDimensions = inequalities.map(_.length - 1)
    if (!inequalityDimensions.forall(_ == inequalityDimensions.head)) {
      throw new IllegalArgumentException(
        "All inequality constraints should be the same length"
      )
    }

    variables.length match {
      case 0 =>
        throw new IllegalArgumentException("some variables must be provided")
      case x if x != inequalityDimensions.head =>
        throw new IllegalArgumentException(
          s"length of variables ($x) should be equal to inequality dimensions ($inequalityDimensions.head)"
        )
      case 1 => {
        // Trivial case
        // TODO: this could probably be solved more trivially using `solutionBounds`
        (variables(0).toSet -- usedValues)
          .filter(
            (variableValue) => {
              testConstraintsSatisfaction(
                inequalitiesSimplified,
                Vector(variableValue)
              )
            }
          )
          .map(Vector(_))
          .toVector
      }
      case _ => {
        // Recursive case
        // - Pick a variable to solve for (variable index 0)
        val variableIndex = 0
        val remainingVariables = variables.patch(variableIndex, Vector(), 1)
        // - For every possible value of that variable:
        (variables(variableIndex).toSet -- usedValues)
          .flatMap((variableValue) => {
            //   - partially solve the remaining inequalities
            val partialSolution = partiallySolveInequalities(
              inequalitiesSimplified,
              variableIndex,
              variableValue
            )

            //   - TODO: check for any all zero constraints or impossible constraints quickly

            //   - recurse
            val solutions = bruteForceInequalities(
              partialSolution,
              remainingVariables,
              usedValues + variableValue
            )

            //    - insert variable value into solutions
            solutions.map((solution) => solution.patch(variableIndex, Vector(variableValue), 0))
          })
          .toVector
      }
    }
  }
}
