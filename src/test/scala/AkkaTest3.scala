object AkkaTest3 {
  import akka.actor.{ Actor, ActorRef, ActorSystem, Props }
  import akka.pattern.ask
  import akka.util.Timeout
  import scala.concurrent.{ Await, Future }
  import scala.concurrent.duration._

  val system = ActorSystem("CoreSystem")

  class Leader(followers: Array[ActorRef]) extends Actor {
    implicit val timeout = Timeout(5 seconds)
    def receive = {
      case "hello" => println("hello back at you")
      case "report" =>
        val result = followers
          .map(_ ? "report")
          .map(_.mapTo[String])
          .map(Await.result(_, timeout.duration))
        println(result.mkString("---\n", "\n", "\n---"))
      case "exit" => { followers.foreach(_ ! "exit"); exit }
      case _ => println("huh?")
    }
  }

  class Follower(index: Int) extends Actor {
    def receive = {
      case "report" => sender ! (index + ": stand ready")
      case "exit" => { println(index + ": stand down"); exit }
      case _ => println("huh?")
    }
  }

  def console(leader: ActorRef): Unit =
    for (line <- io.Source.stdin.getLines) line.split(' ').toList match {
      case "exit" :: Nil => { leader ! "exit"; return }
      case any => any.filterNot("exit" ==).foreach(leader ! _)
    }

  def main(args: Array[String]) = {
    val followers = Array.tabulate(args.head.toInt) { index =>
      system.actorOf(Props(new Follower(index)), name = "follower" + "%03d".format(index))
    }
    val leader = system.actorOf(Props(new Leader(followers)), name = "leader")
    console(leader)
  }
}