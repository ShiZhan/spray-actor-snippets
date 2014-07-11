import akka.actor.{ Actor, ActorRef, ActorSystem, Props }

class Controller extends Actor {
  def receive = {
    case "hello" => println("hello back at you")
    case "exit" => sys.exit
    case _ => println("huh?")
  }
}

object AkkaTest1 extends App {
  def console(controller: ActorRef): Unit =
    for (line <- io.Source.stdin.getLines) line.split(' ').toList match {
      case "exit" :: Nil =>
        controller ! "exit"; return
      case any => any.filterNot(_ == "exit").foreach(controller ! _)
    }

  val system = ActorSystem("CoreSystem")
  val controller = system.actorOf(Props[Controller], name = "controller")
  console(controller)
}