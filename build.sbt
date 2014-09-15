name := "SprayActorSnippets"

version := "1.0"

scalaVersion := Option(System.getProperty("scala.version")).getOrElse("2.10.4")

resolvers ++= Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "spray repo" at "http://repo.spray.io"
)

libraryDependencies ++= {
  val akkaV  = "2.3.5"
  val sprayV = "1.3.1"
  Seq(
    "com.typesafe.akka" % "akka-actor_2.10" % akkaV,
    "io.spray"          % "spray-can"       % sprayV,
    "io.spray"          % "spray-routing"   % sprayV
 )
}
