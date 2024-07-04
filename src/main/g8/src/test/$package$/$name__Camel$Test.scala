package $package$

import $package$.$name;format="Camel"$
import org.apache.spark.sql.Row

final class $name;format="Camel"$Test extends SparkTestHelper {
  "$name;format="Camel"$" should {
    "greet" in {
      val $name;format="camel"$ = new $name;format="Camel"$

      val nameToGreet = "Codely"
      val greeting    = $name;format="camel"$.greet(nameToGreet)

      import testSQLImplicits._
      Seq(greeting).toDF("greeting").collect() shouldBe Array(Row("Hello Codely"))
    }
  }
}
