name := "SprayActorSnippets"

version := "1.0"

scalaVersion := Option(System.getProperty("scala.version")).getOrElse("2.10.4")

resolvers ++= Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "spray repo" at "http://repo.spray.io"
)

libraryDependencies ++= {
  val akkaV  = "2.3.4"
  val sprayV = "1.3.1"
  val jenaV  = "2.11.1"
  val tdbV   = "1.0.1"
  Seq(
    "com.typesafe.akka" % "akka-actor_2.10" % akkaV,
    "io.spray"          % "spray-can"       % sprayV,
    "io.spray"          % "spray-routing"   % sprayV,
    "org.apache.jena"   % "jena-core"       % jenaV excludeAll(ExclusionRule(organization = "org.slf4j")), 
    "org.apache.jena"   % "jena-arq"        % jenaV excludeAll(ExclusionRule(organization = "org.slf4j")), 
    "org.apache.jena"   % "jena-tdb"        % tdbV excludeAll(ExclusionRule(organization = "org.slf4j")),
    "org.slf4j"         % "slf4j-api"       % "1.7.5",
    "org.slf4j"         % "slf4j-log4j12"   % "1.7.5",
    "log4j"             % "log4j"           % "1.2.17"
  )
}
