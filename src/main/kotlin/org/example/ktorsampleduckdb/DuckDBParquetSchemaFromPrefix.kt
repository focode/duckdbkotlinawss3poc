/*
package org.example.ktorsampleduckdb

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request
import software.amazon.awssdk.services.s3.model.S3Object
import java.sql.Connection
import java.sql.DriverManager

fun main() {
    val bucketName = "sparkoperatorpoc1"
    val objectKey = "nfrschema1/"  // Use trailing slash for folder

    // Create an S3 client
    val s3Client = S3Client.builder()
        .region(Region.EU_CENTRAL_1)
        .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
        .build()

    // Extract schema using DuckDB
    extractParquetSchema(bucketName, objectKey, s3Client)
}

fun extractParquetSchema(bucketName: String, objectKey: String, s3Client: S3Client) {
    try {
        // Load DuckDB JDBC driver
        Class.forName("org.duckdb.DuckDBDriver")
        val conn: Connection = DriverManager.getConnection("jdbc:duckdb:")

        // Install and load the httpfs extension
        val stmt = conn.createStatement()
        stmt.execute("INSTALL httpfs")
        stmt.execute("LOAD httpfs")

        // Set AWS credentials and region for DuckDB
        stmt.execute("SET s3_region='eu-central-1'")

        if (objectKey.endsWith("/")) {
            // List all objects in the folder
            val listObjects = s3Client.listObjectsV2(ListObjectsV2Request.builder().bucket(bucketName).prefix(objectKey).build())
            val objects: List<S3Object> = listObjects.contents()

            for (s3Object in objects) {
                if (s3Object.key().endsWith(".parquet")) {
                    val s3Uri = "s3://$bucketName/${s3Object.key()}"
                    processParquetFile(stmt, s3Uri)
                }
            }
        } else {
            // Single file case
            if (objectKey.endsWith(".parquet")) {
                val s3Uri = "s3://$bucketName/$objectKey"
                processParquetFile(stmt, s3Uri)
            } else {
                println("The specified key is not a Parquet file.")
            }
        }

        stmt.close()
        conn.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun processParquetFile(stmt: java.sql.Statement, s3Uri: String) {
    try {
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
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
*/
