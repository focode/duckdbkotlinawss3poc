import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.sql.DriverManager
import java.sql.Connection

@Component
class DuckDBParquetSchemaSchedular {

    @Scheduled(fixedRate = 5000)
    fun consumeMessage() {
        val bucketName = "sparkoperatorpoc1"
        val objectKey =  "sp177.parquet"
        extractParquetSchemaSch(bucketName, objectKey)
    }

    fun extractParquetSchemaSch(bucketName: String, objectKey: String) {
        try {
            // Construct the S3 URI
            val s3Uri = "s3://$bucketName/$objectKey"

            // Load DuckDB JDBC driver
            Class.forName("org.duckdb.DuckDBDriver")
            val conn: Connection = DriverManager.getConnection("jdbc:duckdb:")

            // Install and load the httpfs extension
            val stmt = conn.createStatement()
            stmt.execute("INSTALL httpfs")
            stmt.execute("LOAD httpfs")

            // Set AWS credentials for DuckDB
            stmt.execute("SET s3_region='eu-central-1'")

            // Register S3 file as a DuckDB table
            val createTableQuery = """
            CREATE TABLE s3_table AS 
            SELECT * FROM parquet_scan('$s3Uri')
        """.trimIndent()
            stmt.execute(createTableQuery)

            // Query the schema of the Parquet file
            val rs = stmt.executeQuery("DESCRIBE s3_table")

            // Print the schema
            while (rs.next()) {
                val columnName = rs.getString("column_name")
                val columnType = rs.getString("column_type")
                println("$columnName: $columnType")
            }

            rs.close()
            stmt.close()
            conn.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}



