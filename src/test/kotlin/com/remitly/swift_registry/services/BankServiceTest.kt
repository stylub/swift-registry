package com.remitly.swift_registry.services

import com.remitly.swift_registry.domain.entities.BankEntity
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
import org.junit.jupiter.api.assertThrows

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

    @Test
    fun `save throws error when isHeadquarter true but SWIFT code doesn't end with XXXX`() {
        val invalidRequest = testBankCreateRequestHQA("GLOBUSXX123")

        val exception = assertThrows<IllegalArgumentException> {
            underTest.save(invalidRequest)
        }
        assertThat(exception.message).isEqualTo(
            "isHeadquarter does not match SWIFT code suffix"
        )
    }

    @Test
    fun `save throws error when isHeadquarter false but SWIFT code ends with XXXX`() {
        val invalidRequest = testBankCreateRequestBranchA("GLOBUSXXXX")

        val exception = assertThrows<IllegalArgumentException> {
            underTest.save(invalidRequest)
        }

        assertThat(exception.message).isEqualTo(
            "isHeadquarter does not match SWIFT code suffix"
        )
    }

    @Test
    fun `getOneBySwiftCode returns single correct bank`(){
        val swiftCode = "GLOBUSXXXX"
        underTest.save(testBankCreateRequestHQA("GLOBUSXXXX"))

        val recalledBank = underTest.getOneBySwiftCode(swiftCode)

        assertThat(recalledBank).isNotNull()
        assertThat(recalledBank!!.swiftCode).isEqualTo("GLOBUSXXXX")
        assertThat(recalledBank.bankName).isEqualTo("GlobalBank HQ")
        assertThat(recalledBank.isHeadquarter).isTrue()
        assertThat(recalledBank.countryEntity.countryISO2).isEqualTo("US")
        assertThat(recalledBank.hq).isNull()
    }

    @Test
    fun `delete removes bank from database`() {
        val swiftCode = "GLOBUSXXXX"
        val savedBank = underTest.save(testBankCreateRequestHQA(swiftCode))

        underTest.delete(swiftCode)

        val recalledBank = bankRepository.findByIdOrNull(swiftCode)

        assertThat(recalledBank).isNull()

    }
}