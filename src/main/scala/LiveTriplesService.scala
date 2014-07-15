import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._

class LiveTriplesServiceActor extends Actor with LiveTriplesService {
  def actorRefFactory = context
  def receive = runRoute(ltRoute)
}

trait LiveTriplesService extends HttpService {
  val ltRoute =
    path("") {
      get {
        respondWithMediaType(`text/html`) {
          complete {
            <html>
              <body>
                <h1>Say hello to <i>live-triples</i> on <i>spray-can</i>!</h1>
              </body>
            </html>
          }
        }
      }
    }
}
