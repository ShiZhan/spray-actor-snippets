object LiveTriples {
  import akka.actor.{ ActorSystem, Props }
  import akka.io.IO
  import spray.can.Http
  import akka.pattern.ask
  import akka.util.Timeout
  import scala.concurrent.duration._

  def main(args: Array[String]) = args.toList match {
    case host :: port :: Nil if (port.forall(_.isDigit)) =>
      implicit val system = ActorSystem("CoreSystem")
      val service = system.actorOf(Props[LiveTriplesServiceActor], "live-triples-service")
      implicit val timeout = Timeout(5.seconds)
      IO(Http) ? Http.Bind(service, interface = host, port = port.toInt)
      println("Service started.")
    case _ => println("LiveTriples <host> <port>")
  }
}
