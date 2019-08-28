package solver

trait Tree[+V, +N] {
  def leafValue: Option[V] = this match {
    case n: Node[V, N] => None
    case l: Leaf[V, N] => Some(l.v)
    case Empty         => None
  }

  def nodeValue: Option[N] = this match {
    case n: Node[V, N] => Some(n.v)
    case l: Leaf[V, N] => None
    case Empty         => None
  }

  def left: Option[Tree[V, N]] = this match {
    case n: Node[V, N] => Some(n.l)
    case l: Leaf[V, N] => None
    case Empty         => None
  }

  def right: Option[Tree[V, N]] = this match {
    case n: Node[V, N] => Some(n.r)
    case l: Leaf[V, N] => None
    case Empty         => None
  }
}

case class Node[V, N](v: N, l: Tree[V, N], r: Tree[V, N]) extends Tree[V, N]
case class Leaf[V, N](v: V) extends Tree[V, N]
case object Empty extends Tree[Nothing, Nothing]

case class ConstraintTree[A](
    t: Tree[SingleConstraint[A], Function2[Boolean, Boolean, Boolean]]
) extends Constraint[A, ConstraintTree[A]] {
  type V = SingleConstraint[A]
  type N = Function2[Boolean, Boolean, Boolean]
  def isSatisfiedBy(assignment: Vector[A]): Boolean = {
    t match {
      case n: Node[V, N] =>
        (
          for {
            o <- n.nodeValue;
            l = ConstraintTree[A](n.left getOrElse (Empty))
            r = ConstraintTree[A](n.right getOrElse (Empty))
          } yield o(
            l.isSatisfiedBy(assignment),
            r.isSatisfiedBy(assignment)
          )
        ) getOrElse true
      case l: Leaf[V, N] =>
        (
          for {
            c <- l.leafValue
          } yield c.isSatisfiedBy(assignment)
        ) getOrElse true
      case Empty => true
    }
  }
  def partiallySolve(index: Int, value: A): this.type = {
    copy(
      t match {
        case n: Node[V, N] => {
          Node(
            n.nodeValue.get,
            ConstraintTree[A](n.left.get).partiallySolve(index, value).t,
            ConstraintTree[A](n.right.get).partiallySolve(index, value).t
          )
        }
        case l: Leaf[V, N] => {
          Leaf(l.leafValue.get.partiallySolve(index, value))
        }
        case Empty => Empty
      }
    ).asInstanceOf[this.type]
  }
}
