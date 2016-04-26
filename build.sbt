import com.trueaccord.scalapb.{ScalaPbPlugin => PB}
import sbtrelease._
import ReleaseStateTransformations._
import com.typesafe.sbt.pgp.PgpKeys._

PB.protobufSettings

PB.runProtoc in PB.protobufConfig := (args => com.github.os72.protocjar.Protoc.runProtoc("-v300" +: args.toArray))

scalaVersion := "2.11.7"

name := "shardakka"
organization := "im.actor"
organizationName := "Actor LLC"
organizationHomepage := Some(new URL("https://actor.im/"))

val akkaV = "2.4.4"

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.bintrayRepo("jdgoldie", "maven")
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaV,
  "com.typesafe.akka" %% "akka-persistence" % akkaV,
  "com.typesafe.akka" %% "akka-cluster-tools" % akkaV,
  "com.typesafe.akka" %% "akka-cluster-sharding" % akkaV,
  "com.typesafe.akka" %% "akka-testkit" % akkaV,
  "com.google.guava" % "guava" % "18.0",
  "im.actor" %% "akka-scalapb-serialization" % "0.1.13",
  "com.trueaccord.scalapb" %% "scalapb-runtime" % "0.5.21" % PB.protobufConfig,
  "org.scala-lang.modules" %% "scala-java8-compat" % "0.7.0",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)

parallelExecution in Test := false

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  ReleaseStep(action = Command.process("publishSigned", _)),
  setNextVersion,
  commitNextVersion,
  ReleaseStep(action = Command.process("sonatypeReleaseAll", _)),
  pushChanges
)

unmanagedResourceDirectories in Compile += baseDirectory.value / "src" / "main" / "protobuf"
