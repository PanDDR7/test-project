
name := """test_project"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val myProject = (project in file("."))
  .enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.12.8"

libraryDependencies += guice
libraryDependencies ++= Seq(
  javaJdbc,
  ws,
  "mysql" % "mysql-connector-java" % "8.0.16",
  "org.mariadb.jdbc" % "mariadb-java-client" % "1.4.4"
)
playEbeanModels in Compile := Seq("models.*")
