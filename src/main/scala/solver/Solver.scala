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
      inequalities: Vector[SingleConstraint[A]]
  ): Vector[SingleConstraint[A]] = {
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
      inequalities: Vector[SingleConstraint[A]]
  ): Vector[Tuple2[Option[A], Option[A]]] = ???
  //

  /**
    * Determines if a system of constraints expressed as a vector over type A is satisfied by a particular choice of values
    */
  def testSingleConstraintsSatisfaction[A: Numeric](
      inequalities: Vector[SingleConstraint[A]],
      assignment: Vector[A]
  ): Boolean = {
    inequalities forall { (inequality) =>
      inequality.isSatisfiedBy(assignment)
    }
  }

  def partiallySolveInequalities[A: Numeric](
      inequalities: Vector[SingleConstraint[A]],
      index: Int,
      value: A
  ): Vector[SingleConstraint[A]] = {
    inequalities.map(
      (inequality) => inequality.partiallySolve(index, value)
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
      inequalities: Vector[SingleConstraint[Int]],
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
          s"length of variables ($x) should equal width of inequalities"
        )
      case 1 => {
        // Trivial case
        // TODO: this could probably be solved more trivially using `solutionBounds`
        (variables(0).toSet -- usedValues)
          .filter((variableValue) => {
            testSingleConstraintsSatisfaction(
              inequalitiesSimplified,
              Vector(variableValue)
            )
          })
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
            solutions.map(
              (solution) =>
                solution.patch(variableIndex, Vector(variableValue), 0)
            )
          })
          .toVector
      }
    }
  }
}
