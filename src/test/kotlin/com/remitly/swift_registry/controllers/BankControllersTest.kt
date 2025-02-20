package com.remitly.swift_registry.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.remitly.swift_registry.domain.dto.BankDto
import com.remitly.swift_registry.domain.entities.BankEntity
import com.remitly.swift_registry.domain.entities.CountryEntity
import com.remitly.swift_registry.services.BankService
import com.remitly.swift_registry.testBankCreateRequestA
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class BankControllersTest @Autowired constructor(
    private val mockMvc: MockMvc,
    @MockkBean val bankService: BankService
){

    val objectMapper = ObjectMapper()

    @BeforeEach
    fun beforeEach() {
        every {
            bankService.save(any())
        } answers {
            firstArg()
        }
    }

    @Test
    fun `test creating new HQ bank`(){
        mockMvc.post("/v1/swift-codes"){
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                testBankCreateRequestA("GLOBUSXXXXX")
            )
        }.andExpect {
            status { isCreated() }
        }
    }

    @Test
    fun `test creating adding branch without HQ available`(){
        mockMvc.post("/v1/swift-codes"){
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                "GLOBUSXX123"
            )
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `test that create Bank saves the Bank`() {

        mockMvc.post("/v1/swift-codes") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                testBankCreateRequestA("GLOBUSXXXXX")
            )
        }

        val expected = BankDto(
            swiftCode = "GLOBUSXXXXX",
            address = "HQ Address 123",
            bankName = "GlobalBank HQ",
            isHeadquarter = true,
            countryISO2 = "US",
            countryName = "United States",
            branches = null
        )

        val expectedRequest = testBankCreateRequestA("GLOBUSXXXXX")

        verify { bankService.save(expectedRequest) }
    }
}