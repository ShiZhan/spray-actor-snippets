package Kernel

object Service {
  import akka.actor.Actor
  import spray.routing._
  import spray.http._
  import spray.http.MediaTypes._
  import spray.http.Uri.apply
  import spray.httpx.marshalling.ToResponseMarshallable.isMarshallable
  import spray.routing.Directive.pimpApply

  class LiveTriplesService extends Actor with HttpService {
    def actorRefFactory = context
    val routes =
      get {
        path(Rest) { pathRest =>
          complete { pathRest }
        }
      }

    def receive = runRoute(routes)
  }
}

object ServiceWrapper {
  import akka.actor.{ ActorSystem, Props }
  import akka.io.IO
  import spray.can.Http
  import akka.pattern.ask
  import akka.util.Timeout
  import scala.concurrent.duration._

  private lazy val defaultIP = java.net.InetAddress.getLocalHost.getHostAddress

  def runService(host: String = defaultIP, port: Int = 8080) = {
    implicit val system = ActorSystem("CoreSystem")
    val ltService = system.actorOf(Props[Service.LiveTriplesService], "live-triples")
    implicit val timeout = Timeout(5.seconds)
    IO(Http) ? Http.Bind(ltService, interface = host, port = port.toInt)
    println("Service started, input [exit] to quit.")
    while (readLine != "exit") {}
    system.shutdown()
  }
}