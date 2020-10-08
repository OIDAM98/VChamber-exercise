lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "dev.odealva",
      scalaVersion := "2.13.3"
    )),
    name := "chamber-exercise"
  )

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.2" % Test