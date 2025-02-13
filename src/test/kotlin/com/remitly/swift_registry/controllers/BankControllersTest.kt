package com.remitly.swift_registry.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.remitly.swift_registry.domain.dto.BankCreateRequest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class BankControllersTest @Autowired constructor(private val mockMvc: MockMvc){

    val objectMapper = ObjectMapper()

    @Test
    fun `test creating new HQ bank`(){
        mockMvc.post("/v1/swift-codes"){
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                BankCreateRequest(
                    address = "HQ Address 123",
                    bankName = "GlobalBank HQ",
                    countryISO2 = "US",
                    countryName = "United States",
                    headquarter = true,
                    swiftCode = "GLOBUSXXXXX"
                )
            )
        }.andExpect {
            status { isCreated() }
        }
    }
}