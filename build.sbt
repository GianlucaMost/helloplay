name := "helloPlay"

version := "1.0-SNAPSHOT"

Keys.fork in Test := false

//parallelExecution in Test := false

libraryDependencies ++= Seq(
  //javaJdbc,
  //javaEbean,
  cache,
  javaJpa,
  "mysql" % "mysql-connector-java" % "5.1.25",
  "org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final",
  "org.mindrot" % "jbcrypt" % "0.3m",
  "rome" % "rome" % "1.0"
)

play.Project.playJavaSettings
