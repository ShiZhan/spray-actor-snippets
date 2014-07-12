object AkkaTest4 {
  import akka.actor.{ Actor, ActorRef, ActorSystem, Props }
  import scala.util.Random

  sealed abstract class Messages
  case class UPDATE(value: Int) extends Messages
  case class STOP() extends Messages
  case class HALT() extends Messages

  val nameOfMonitor = "Monitor"
  val nameOfVertexPrefix = "Vertex"
  def nameOfVertex(index: Int) = nameOfVertexPrefix + "%03d".format(index)
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
      case UPDATE(newValue) =>
        if (newValue < value) {
          value = newValue
          if (!in.isEmpty) {
            monitor ! UPDATE(in.length)
            for ((length, next) <- in) {
              println(s"$index->$next:$value+$length")
              vertices(next) ! UPDATE(value + length)
            }
          }
        }
        monitor ! UPDATE(-1)
      case STOP => sys.exit
    }
  }

  class Monitor extends Actor {
    var counter = 0
    val vertices = context.actorSelection(up + nameOfVertexPrefix + "*")
    def receive = {
      case UPDATE(delta) => {
        counter += delta
        if (counter == 0) { vertices ! STOP; sys.exit }
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
    val monitor = system.actorOf(Props[Monitor], name = nameOfMonitor)
    monitor ! UPDATE(1)
    vertices(vertexTotal - 1) ! UPDATE(0)
  }
}