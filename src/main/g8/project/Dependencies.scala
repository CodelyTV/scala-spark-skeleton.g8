import sbt._

object Dependencies {
  private val prod = Seq(
    "com.github.nscala-time" %% "nscala-time"     % "$nscala-time_version$",
    "com.lihaoyi"            %% "pprint"          % "$pprint_version$",
    "org.apache.spark"       %% "spark-core"      % "$spark_version$" % Provided,
    "org.apache.spark"       %% "spark-sql"       % "$spark_version$" % Provided,
    "org.apache.spark"       %% "spark-streaming" % "$spark_version$" % Provided
  )
  private val test = Seq(
    "org.scalatest" %% "scalatest" % "$scalatest_version$",
    "org.scalamock" %% "scalamock" % "$scalamock_version$"
  ).map(_ % Test)

  val all = prod ++ test
}
