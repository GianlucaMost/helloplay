name := "helloPlay"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  //javaJdbc,
  //javaEbean,
  cache,
  javaJpa,
  "mysql" % "mysql-connector-java" % "5.1.25",
  "org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final"
)     

play.Project.playJavaSettings
