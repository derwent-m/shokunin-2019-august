package solver

import org.scalatest.{Matchers, FunSuite}

/** @version 1.0.0 */
class LogicTest extends FunSuite with Matchers {
  test("and") {
    Logic.and(true, true) should be(true)
    Logic.and(false, true) should be(false)
    Logic.and(true, false) should be(false)
    Logic.and(false, false) should be(false)
  }

  test("or") {
    Logic.or(true, true) should be(true)
    Logic.or(false, true) should be(true)
    Logic.or(true, false) should be(true)
    Logic.or(false, false) should be(false)
  }
}
