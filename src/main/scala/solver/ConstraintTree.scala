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

case class Node[V, N]( v: N, l: Tree[V, N], r: Tree[V, N] ) extends Tree[V, N]
case class Leaf[V, N](v: V) extends Tree[V, N]
case object Empty extends Tree[Nothing, Nothing]

case class ConstraintTree[A](t:Tree[SingleConstraint[A], Function2[Boolean, Boolean, Boolean]])
    extends Constraint[A, ConstraintTree[A]]
{
  type V = SingleConstraint[A]
  type N = Function2[Boolean, Boolean, Boolean]
  def isSatisfiedBy(assignment: Vector[A]): Boolean = {
    t match {
      case n: Node[V, N] => true
      case l: Leaf[V, N] => l.leafValue match {
        case Some(constraint) => constraint.isSatisfiedBy(assignment)
        case None => true
      }
      case Empty         => true
    }
  }
  def partiallySolve(index: Int, value: A): this.type = ???
}
