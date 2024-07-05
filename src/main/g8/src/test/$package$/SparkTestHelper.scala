package $package$

import org.apache.commons.io.FileUtils
import org.apache.spark.sql.{SQLContext, SQLImplicits, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.io.File
import java.nio.file.Files
import scala.reflect.io.Directory

trait SparkTestHelper
  extends AnyWordSpec
    with BeforeAndAfterEach
    with BeforeAndAfterAll
    with Matchers {

  private val sparkSession = SparkSession
    .builder()
    .master("local[*]")
    .appName("test-spark-session")
    .config(sparkConfiguration)
    //.enableHiveSupport() uncomment this if you want to use Hive
    .getOrCreate()

  protected var tempDir: String = _

  protected implicit def spark: SparkSession = sparkSession

  protected def sc: SparkContext = sparkSession.sparkContext

  protected def sparkConfiguration: SparkConf =
    new SparkConf()
    /* Uncomment this if you want to use Delta Lake

      .set("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension")
      .set(
        "spark.sql.catalog.spark_catalog",
        "org.apache.spark.sql.delta.catalog.DeltaCatalog"
      )
    */

  override protected def beforeAll(): Unit = {
    super.beforeAll()
    clearTemporaryDirectories()
  }

  override protected def beforeEach(): Unit = {
    super.beforeEach()
    tempDir = Files.createTempDirectory(this.getClass.toString).toString
  }

  override protected def afterAll(): Unit = {
    super.afterAll()
    sparkSession.stop()
    SparkSession.clearActiveSession()
    SparkSession.clearDefaultSession()
    clearTemporaryDirectories()
  }

  override protected def afterEach(): Unit = {
    super.afterEach()
    new Directory(new File(tempDir)).deleteRecursively()
    spark.sharedState.cacheManager.clearCache()
    spark.sessionState.catalog.reset()
  }

  protected object testSQLImplicits extends SQLImplicits {
    protected override def _sqlContext: SQLContext = sparkSession.sqlContext
  }

  private def clearTemporaryDirectories(): Unit = {
    val warehousePath = new File("spark-warehouse").getAbsolutePath
    FileUtils.deleteDirectory(new File(warehousePath))

    val metastoreDbPath = new File("metastore_db").getAbsolutePath
    FileUtils.deleteDirectory(new File(metastoreDbPath))
  }
}
