import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._

class LiveTriplesServiceActor extends Actor with LiveTriplesService {
  def actorRefFactory = context
  def receive = runRoute(routes)
}

trait LiveTriplesService extends HttpService {
  val routes =
    get {
      pathSingleSlash {
        redirect("/triples", StatusCodes.Found)
      } ~ path("triples") {
        respondWithMediaType(`text/html`) {
          complete {
            <html>
              <body>
                <h1>Say hello to <i>live-triples</i> on <i>spray-can</i>!</h1>
              </body>
            </html>
          }
        }
      } ~ path("status") {
        complete {
          <html>
            <h1>System status</h1>
          </html>
        }
      }
    }
}
