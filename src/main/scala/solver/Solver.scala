package solver
/** Provides classes manipulating matrices , representing systems of inequalities
 */


object Solver {
  /** Simplify a set of inequalities in the form of a matrix over type A
   *
   *  @param inequalities a system of inequalities expressed as a matrix over type A.
   *  @return the simplified matrix
   */
  def simplifyInequalities[A]( inequalities : Vector[Vector[A]] ) : Vector[A] = ???
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
  def solutionBounds[A]( inequalities : Vector[Vector[A]] ) : Vector[Tuple2[Option[A], Option[A]]] = ???
    //

  /**
   * Determines if constraint expressed as a vector over type A is satisfied by a particular choice of values
   */
  def testConstraintSatisfaction[A:Numeric]( inequality : Vector[A], values : Vector[A]) : Boolean = {
    val left = (inequality zip values).map({
      case (coeff, value) => implicitly[Numeric[A]].times(coeff, value)
    }).sum
    val right = inequality.last
    implicitly[Numeric[A]].lt(left, right)
  }

  /**
   * Partially solves an inequality, rewrites the inequality in terms of this new information
   */
  def partiallySolveInequality[A:Numeric]( inequality : Vector[A], index: Int, value : A ) : Vector[A] = ???

  /**
   * Recursively Brute Forces all solutions which solve a set of integer linear inequalities
   * for variables that are specified as ranges
   */
  def bruteForceInequalities( inequalities : Vector[Vector[Int]], variables : Vector[Range] ) : Vector[Vector[Int]] = {

  }
}
