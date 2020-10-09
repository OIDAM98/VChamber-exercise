import sbt.protocol.ExecCommand

resolvers += Resolver.sonatypeRepo("snapshots")

lazy val root = (project in file("."))
  .settings(
    inThisBuild(
      List(
        organization := "dev.odealva",
        scalaVersion := "2.13.3"
      )
    ),
    name := "chamber-exercise",
    libraryDependencies ++= Seq("org.scalatest" %% "scalatest" % "3.2.2" % Test)
  )
  .aggregate(docker)

import com.typesafe.sbt.packager.docker._

lazy val docker = (project in file("."))
  .enablePlugins(DockerPlugin)
  .enablePlugins(AshScriptPlugin)
  .settings(
    name := "chamber-test",
    packageName in Docker := "chamber-tests",
    resolvers += Resolver.sonatypeRepo("snapshots"),
    Defaults.itSettings,
    dockerBaseImage := "openjdk:8u201-jre-alpine3.9",
    dockerUpdateLatest := true,
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.2" % Test,
    )
  )
