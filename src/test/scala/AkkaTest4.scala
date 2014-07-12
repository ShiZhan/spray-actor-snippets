object AkkaTest4 {
  import akka.actor.{ Actor, ActorRef, ActorSystem, Props }
  import scala.util.Random

  def edgeIn(index: Int)(implicit m: Array[Array[Int]]) =
    m.map(_(index)).zipWithIndex.filter(_._1 != 0)

  def edgeOut(index: Int)(implicit m: Array[Array[Int]]) =
    m(index).zipWithIndex.filter(_._1 != 0)

  sealed abstract class Messages
  case class UPDATE(value: Int) extends Messages
  case class STOP() extends Messages
  case class HALT() extends Messages

  val nameOfMonitor = "Monitor"
  def nameOfVertex(index: Int) = "Vertex%03d".format(index)
  val up = "../"

  type Matrix = Array[Array[Int]]
  class Vertex(index: Int, m: Matrix) extends Actor {
    val vertexTotal = m.length
    val in = m.map(_(index)).zipWithIndex.filter(_._1 != 0)
    val out = m(index).zipWithIndex.filter(_._1 != 0)
    val monitor = context.actorSelection(up + nameOfMonitor)
    val vertices =
      Array.tabulate(vertexTotal)(index => context.actorSelection(up + nameOfVertex(index)))
    var value = Int.MaxValue
    def receive = {
      case UPDATE(newValue) => if (newValue < value) {
        value = newValue
        if (!in.isEmpty) for ((length, next) <- in) {
          println(s"$index->$next:$value+$length")
          monitor ! UPDATE(in.length - 1)
          vertices(next) ! UPDATE(value + length)
        }
        else monitor ! UPDATE(-1)
      } else monitor ! UPDATE(-1)
      case STOP => sys.exit
    }
  }

  class Monitor(vertices: Array[ActorRef]) extends Actor {
    var counter = 0
    def receive = {
      case UPDATE(delta) => {
        counter += delta
        if (counter == 0) { vertices.foreach(_ ! STOP); sys.exit }
      }
    }
  }

  def main(args: Array[String]) = {
    val vertexTotal = args.head.toInt
    val m = Array.fill(vertexTotal, vertexTotal)(Random.nextInt(2))
    val system = ActorSystem("CoreSystem")

    val vertices = Array.tabulate(vertexTotal) { index =>
      system.actorOf(Props(new Vertex(index, m)), name = nameOfVertex(index))
    }
    val monitor = system.actorOf(Props(new Monitor(vertices)), name = nameOfMonitor)
    monitor ! UPDATE(1)
    vertices(vertexTotal - 1) ! UPDATE(0)
  }
}