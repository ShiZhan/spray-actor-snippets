object AkkaTest5 {
  import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem, Props }
  import scala.util.Random

  def isAllDigits(x: String) = x forall Character.isDigit

  class Leader(followers: Array[ActorRef]) extends Actor with ActorLogging {
    val total = followers.length
    def receive = {
      case "start" => followers.foreach(_ ! "start")
      case "report" => followers.foreach(_ ! "report")
      case "all" => followers.foreach(_ ! "turn")
      case index: String if (isAllDigits(index)) =>
        val i = index.toInt
        if (i < total) followers(i) ! "turn"
      case "exit" => { followers.foreach(_ ! "exit"); sys.exit }
      case _ => log.error("start|report|all|<number>|exit")
    }
  }

  trait TurnBehavior {
    this: Actor with ActorLogging =>
    import context._

    val common: Receive = {
      case "exit" => sys.exit
      case _ => log.error("huh?")
    }

    val start: Receive = {
      case "start" =>
        if (Random.nextBoolean)
          become(left orElse common)
        else
          become(right orElse common)
    }

    val left: Receive = {
      case "report" => log.info("Left")
      case "turn" => become(right orElse common)
    }

    val right: Receive = {
      case "report" => log.info("Right")
      case "turn" => become(left orElse common)
    }
  }

  class Follower(index: Int) extends Actor with ActorLogging with TurnBehavior {
    def receive = start orElse common
  }

  def console(leader: ActorRef): Unit =
    for (line <- io.Source.stdin.getLines) line.split(' ').toList match {
      case "exit" :: Nil => { leader ! "exit"; return }
      case any => any.filterNot(i => i == "exit" || i == "").foreach(leader ! _)
    }

  def main(args: Array[String]) = {
    val system = ActorSystem("CoreSystem")
    val followers = Array.tabulate(args.head.toInt) { index =>
      system.actorOf(Props(new Follower(index)), name = "follower" + "%03d".format(index))
    }
    val leader = system.actorOf(Props(new Leader(followers)), name = "leader")
    console(leader)
  }
}