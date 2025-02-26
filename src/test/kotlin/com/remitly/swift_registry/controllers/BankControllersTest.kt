package com.remitly.swift_registry.controllers

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.ninjasquad.springmockk.MockkBean
import com.remitly.swift_registry.*
import com.remitly.swift_registry.domain.dto.BankCreateRequest
import com.remitly.swift_registry.domain.entities.BankEntity
import com.remitly.swift_registry.domain.entities.CountryEntity
import com.remitly.swift_registry.services.BankService
import com.remitly.swift_registry.services.CountryService
import io.mockk.every
import io.mockk.verify
import org.apache.coyote.BadRequestException
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

private const val BASE_URL = "/v1/swift-codes"

@SpringBootTest
@AutoConfigureMockMvc
class BankControllersTest @Autowired constructor(
    private val mockMvc: MockMvc,
    @MockkBean private val  bankService: BankService,
){

    private val objectMapper = JsonMapper.builder()
        .addModule(KotlinModule.Builder().build())
        .configure(MapperFeature.USE_STD_BEAN_NAMING, true)
        .build()

    @Test
    fun `test creating new bank successfully`() {
        val testCountryEntity = testCountryEntityUS()
        val mockResponse = testBankCreateRequestHQA("GLOBUSXXXXX")
            .toBankEntity(countryEntity = testCountryEntity, hq = null)

        every { bankService.save(any()) } returns mockResponse

        mockMvc.post(BASE_URL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                testBankCreateRequestHQA("GLOBUSXXXXX")
            )
        }.andExpect {
            status { isCreated() }
            jsonPath("$.message") { value("Bank created successfully") }
        }

        verify { bankService.save(any()) }
    }

    @Test
    fun `test that test if error message is propagated correctly`() {
        val errorMessage = "Country name Incorrect Country Name does not match with US"
        every {
            bankService.save(any())
        } throws BadRequestException(errorMessage)

        mockMvc.post(BASE_URL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                BankCreateRequest(
                    swiftCode = "GLOBUSXX123",
                    address = "Test Address",
                    bankName = "Test Bank",
                    countryISO2 = "US",
                    countryName = "Incorrect Country Name",
                    isHeadquarter = false
                )
            )
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.message") { value("Error: $errorMessage") }
        }
    }

    @Test
    fun `retrieve branch bank returns 200 with basic DTO`() {
        val swiftCode = "GLOBUSXX123"
        val country = CountryEntity("US", "United States")
        val branch = BankEntity(
            swiftCode = swiftCode,
            address = "Branch Address",
            bankName = "NY Branch",
            countryEntity = country,
            isHeadquarter = false,
            hq = testBankEntityA(swiftCode = "GLOBUSXXXX")
        )

        every { bankService.getOneBySwiftCode(swiftCode) } returns branch

        mockMvc.get("$BASE_URL/$swiftCode") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            jsonPath("$.swiftCode") { value(swiftCode) }
            jsonPath("$.isHeadquarter") { value(false) }
        }
    }

    @Test
    fun `retrieve non-existent bank returns 404`() {
        val invalidSwift = "INVALID123"
        every { bankService.getOneBySwiftCode(invalidSwift) } returns null

        mockMvc.get("$BASE_URL/$invalidSwift") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNotFound() }
        }
    }

    @Test
    fun `DTO contains correct country information`() {
        val swiftCode = "GLOBUSXXXX"
        val country = testCountryEntityUS()
        val bank = BankEntity(
            swiftCode = swiftCode,
            address = "American Address",
            bankName = "US HQ",
            countryEntity = country,
            isHeadquarter = true,
            hq = null
        )

        every { bankService.getOneBySwiftCode(swiftCode) } returns bank

        mockMvc.get("$BASE_URL/$swiftCode") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            jsonPath("$.countryISO2") { value("US") }
            jsonPath("$.countryName") { value("United States") }
        }
    }

    @Test
    fun `test that delete Bank returns HTTP 200 on successful delete and return message`() {
        every {
            bankService.delete(any())
        } answers {}

        mockMvc.delete("${BASE_URL}/GLOBUSXXXX") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            jsonPath("$.message") {value("Bank deleted successfully")}
        }
    }

    @Test
    fun `test that delete Bank returns HTTP 400 on exception while deleting and return correct message`() {
        val swiftCode = "GLOBUSXXXX"
        val errorMessage = "Bank with SWIFT code $swiftCode not found"
        every {
            bankService.delete(any())
        } throws BadRequestException(errorMessage)

        mockMvc.delete("${BASE_URL}/$swiftCode") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.message") {value("Error: $errorMessage")}
        }
    }
}