name := "SprayActorSnippets"

version := "1.0"

scalaVersion := Option(System.getProperty("scala.version")).getOrElse("2.11.6")

resolvers ++= Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "spray repo"          at "http://repo.spray.io"
)

libraryDependencies ++= {
  val akkaV  = "2.3.9"
  val sprayV = "1.3.2"
  Seq(
    "com.typesafe.akka" % "akka-actor_2.11"    % akkaV,
    "io.spray"          % "spray-can_2.11"     % sprayV,
    "io.spray"          % "spray-routing_2.11" % sprayV
  )
}
