package com.remitly.swift_registry.controllers

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.ninjasquad.springmockk.MockkBean
import com.remitly.swift_registry.domain.entities.BankEntity
import com.remitly.swift_registry.domain.entities.CountryEntity
import com.remitly.swift_registry.services.BankService
import com.remitly.swift_registry.services.CountryService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

private const val BASE_URL = "/v1/swift-codes/country"

@SpringBootTest
@AutoConfigureMockMvc
class CountryControllersTest @Autowired constructor(
    private val mockMvc: MockMvc,
    @MockkBean private val countryService: CountryService,
){
    @Test
    fun `getBanksByCountry returns 200 with empty banks list`() {
        val countryISO2 = "FR"
        val countryEntity = CountryEntity(countryISO2, "France", emptyList())
        every { countryService.get(countryISO2) } returns countryEntity

        mockMvc.get("$BASE_URL/$countryISO2") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            jsonPath("$.countryISO2") { value("FR") }
            jsonPath("$.banks") { emptyArray<Any>() }
        }
    }

    @Test
    fun `getBanksByCountry returns 404 for non-existent country`() {
        val invalidISO2 = "XX"
        every { countryService.get(invalidISO2) } returns null

        mockMvc.get("$BASE_URL/$invalidISO2") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNotFound() }
        }
    }
}