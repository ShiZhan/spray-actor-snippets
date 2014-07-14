import akka.actor.ActorSystem
import spray.routing.SimpleRoutingApp

object SprayTest1 extends App with SimpleRoutingApp {
  implicit val system = ActorSystem("CoreSystem")

  startServer(interface = "localhost", port = 8080) {
    path("hello") {
      get {
        complete {
          <h1>Hello Spray!</h1>
        }
      }
    }
  }
}
