package org.example.ktorsampleduckdb

import DuckDBParquetSchemaSchedular
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class KtorSampleDuckdbApplication {

    @Bean
    fun duckDBParquetSchemaSchedular(): DuckDBParquetSchemaSchedular {
        return DuckDBParquetSchemaSchedular()
    }
}

fun main(args: Array<String>) {
    runApplication<KtorSampleDuckdbApplication>(*args) {
    }
}
