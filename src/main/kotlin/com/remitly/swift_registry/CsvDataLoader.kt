package com.remitly.swift_registry

import com.opencsv.CSVReader
import com.remitly.swift_registry.domain.dto.BankCreateRequest
import com.remitly.swift_registry.services.BankService
import org.apache.coyote.BadRequestException
import org.springframework.boot.CommandLineRunner
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.io.InputStreamReader

@Component
class CsvDataLoader(
    private val bankService: BankService
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        val csvRows = parseCsv("SWIFT_CODES.csv")

        val (hqRows, branchRows) = csvRows.partition {
            it.swiftCode.endsWith("XXX")
        }

        hqRows.forEach { row ->
            val hq = BankCreateRequest(
                swiftCode = row.swiftCode,
                bankName = row.name,
                address = row.address,
                countryISO2 = row.countryIso2,
                countryName = row.countryName,
                isHeadquarter = true
            )
            addBank(hq)
        }

        branchRows.forEach { row ->
            val branch = BankCreateRequest(
                swiftCode = row.swiftCode,
                bankName = row.name,
                address = row.address,
                countryISO2 = row.countryIso2,
                countryName = row.countryName,
                isHeadquarter = false
            )
            addBank(branch)
        }
    }

    private fun parseCsv(fileName: String): List<CsvRow> {
        val csvFile = ClassPathResource(fileName).inputStream

        CSVReader(InputStreamReader(csvFile)).use { reader ->
            reader.readNext()
            val rows = reader.readAll()

            return rows.map { row ->
                CsvRow(
                    countryIso2 = row[0],
                    swiftCode = row[1],
                    name = row[3],
                    address = row[4],
                    countryName = row[6]
                )
            }
        }
    }

    private fun addBank(bankCreateRequest: BankCreateRequest){
        try{
            bankService.save(bankCreateRequest)
        } catch (e: BadRequestException) {
            println("Skipping branch ${bankCreateRequest.swiftCode}: ${e.message}")
        }
    }

    private data class CsvRow(
        val countryIso2: String,
        val swiftCode: String,
        val name: String,
        val address: String,
        val countryName: String
    )
}

