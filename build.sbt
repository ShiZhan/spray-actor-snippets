name := "LiveTriples"

version := "1.0"

scalaVersion := Option(System.getProperty("scala.version")).getOrElse("2.10.4")

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
 
libraryDependencies ++= Seq(
  "com.typesafe.akka" % "akka-actor_2.10" % "2.3.4",
  "org.apache.jena" % "jena-core" % "2.11.1" excludeAll(ExclusionRule(organization = "org.slf4j")), 
  "org.apache.jena" % "jena-arq" % "2.11.1" excludeAll(ExclusionRule(organization = "org.slf4j")), 
  "org.apache.jena" % "jena-tdb" % "1.0.1" excludeAll(ExclusionRule(organization = "org.slf4j")),
  "org.slf4j" % "slf4j-api" % "1.7.5",
  "org.slf4j" % "slf4j-log4j12" % "1.7.5",
  "log4j" % "log4j" % "1.2.17"
)
