object AkkaTest2 {
  import akka.actor.{ Actor, ActorRef, ActorSystem, Props }

  val system = ActorSystem("CoreSystem")

  class Leader(followers: Array[ActorRef]) extends Actor {
    def receive = {
      case "hello" => println("hello back at you")
      case "report" => followers.foreach(_ ! "report")
      case "exit" => { followers.foreach(_ ! "exit"); exit }
      case _ => println("huh?")
    }
  }

  class Follower(index: Int) extends Actor {
    def receive = {
      case "report" => println(index + ": stand ready")
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