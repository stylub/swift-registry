package com.remitly.swift_registry.services

import com.remitly.swift_registry.domain.dto.BankCreateRequest
import com.remitly.swift_registry.domain.entities.BankEntity
import com.remitly.swift_registry.domain.entities.CountryEntity
import com.remitly.swift_registry.repositories.BankRepository
import com.remitly.swift_registry.repositories.CountryRepository
import com.remitly.swift_registry.testBankCreateRequestBranchA
import com.remitly.swift_registry.testBankCreateRequestHQA
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post

@SpringBootTest
@Transactional
class BankServiceTest @Autowired constructor(
    private val underTest: BankService,
    private val bankRepository: BankRepository,
    private val countryRepository: CountryRepository)
{
    @AfterEach
    fun cleanup() {
        bankRepository.deleteAll()
        countryRepository.deleteAll()
    }

    @Test
    fun `test that save persists the Bank in the database`() {
        val savedBank = underTest.save(testBankCreateRequestHQA("GLOBUSXXXX"))

        val recalledBank = bankRepository.findByIdOrNull(savedBank.swiftCode)

        assertThat(recalledBank).isNotNull()
        assertThat(recalledBank!!.swiftCode).isEqualTo("GLOBUSXXXX")
        assertThat(recalledBank.bankName).isEqualTo("GlobalBank HQ")
        assertThat(recalledBank.isHeadquarter).isTrue()
        assertThat(recalledBank.countryEntity.countryISO2).isEqualTo("US")
        assertThat(recalledBank.hq).isNull()
    }

    @Test
    fun `test that Bank also saves Country in the database`() {
        val savedBank = underTest.save(testBankCreateRequestHQA("GLOBUSXXXXX"))

        val recalledCountry = countryRepository.findByIdOrNull(savedBank.countryEntity.countryISO2)

        assertThat(recalledCountry).isNotNull()
        assertThat(recalledCountry!!.countryISO2).isEqualTo("US")
        assertThat(recalledCountry.countryName).isEqualTo("United States")
    }

    @Test
    fun `test that branch is getting connected to HQ when created`(){
        val savedHQ = underTest.save(testBankCreateRequestHQA("GLOBUSXXXXX"))
        val savedBranch = underTest.save(testBankCreateRequestBranchA("GLOBUSXX123"))

        val recalledBank = bankRepository.findByIdOrNull(savedBranch.swiftCode)

        assertThat(recalledBank).isNotNull()
        assertThat(recalledBank!!.hq).isEqualTo(savedHQ)
    }
//    @Test
//    fun `test creating new bank where SWIFT does not match isHeadquarter`() {
//        mockMvc.post("/v1/swift-codes") {
//            contentType = MediaType.APPLICATION_JSON
//            content = objectMapper.writeValueAsString(
//                testBankCreateRequestBranchA("GLOBUSXXXXX")
//            )
//        }.andExpect {
//            status { isBadRequest() }
//            jsonPath("$.message") { value("Error: isHeadquarter does not match SWIFT code suffix") }
//        }
//        mockMvc.post("/v1/swift-codes") {
//            contentType = MediaType.APPLICATION_JSON
//            content = objectMapper.writeValueAsString(
//                testBankCreateRequestHQA("GLOBUSXX123")
//            )
//        }.andExpect {
//            status { isBadRequest() }
//            jsonPath("$.message") { value("Error: isHeadquarter does not match SWIFT code suffix") }
//        }
//    }

}