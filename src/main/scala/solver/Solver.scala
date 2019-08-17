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
   * Brute Forces all solutions which solve a set of integer linear inequalities for variables that are specified as ranges
   */
  def bruteForceInequalities( inequalities : Vector[Vector[Int]], variables : Vector[Range] ) : Vector[Vector[Int]] = ???
}
