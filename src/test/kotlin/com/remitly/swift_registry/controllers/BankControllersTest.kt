package com.remitly.swift_registry.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.remitly.swift_registry.domain.dto.BankCreateRequest
import com.remitly.swift_registry.domain.dto.BankDto
import org.junit.jupiter.api.Test
import org.mockito.internal.matchers.Null
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import kotlin.test.assertTrue

@SpringBootTest
@AutoConfigureMockMvc
class BankControllersTest @Autowired constructor(private val mockMvc: MockMvc){

    val objectMapper = ObjectMapper()

    @Test
    fun `test creating new bank`(){
        mockMvc.post("/v1/swift-codes"){
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                BankCreateRequest(
                    address = "Some Test Address 20/5",
                    bankName = "ExampleBank",
                    countryISO2 = "PL",
                    countryName = "Poland",
                    isHeadquarter = false,
                    swiftCode = "AAAAAAAAAAA"
                )
            )
        }.andExpect {
            status { isCreated() }
        }
    }
}