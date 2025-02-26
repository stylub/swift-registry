package com.remitly.swift_registry.services
import com.remitly.swift_registry.domain.entities.CountryEntity
import com.remitly.swift_registry.repositories.CountryRepository
import jakarta.transaction.Transactional
import org.apache.coyote.BadRequestException
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.assertThrows

@SpringBootTest
@Transactional
class CountryServiceTest @Autowired constructor(
    private val underTest: CountryService,
    private val countryRepository: CountryRepository
) {

    @AfterEach
    fun cleanup() {
        countryRepository.deleteAll()
    }

    @Test
    fun `findOrCreateCountry creates new country when not exists`() {
        val countryISO2 = "US"
        val countryName = "United States"

        val country = underTest.findOrCreateCountry(countryISO2, countryName)

        val retrieved = countryRepository.findByIdOrNull(countryISO2)
        assertThat(retrieved).isNotNull()
        assertThat(retrieved!!.countryISO2).isEqualTo(countryISO2)
        assertThat(retrieved.countryName).isEqualTo(countryName)
    }

    @Test
    fun `findOrCreateCountry returns existing country when matches`() {
        val countryISO2 = "US"
        val countryName = "United States"
        countryRepository.save(CountryEntity(countryISO2 = countryISO2, countryName = countryName))

        val result = underTest.findOrCreateCountry(countryISO2, countryName)

        assertThat(result.countryISO2).isEqualTo(countryISO2)
        assertThat(result.countryName).isEqualTo(countryName)
        assertThat(countryRepository.count()).isEqualTo(1)
    }

    @Test
    fun `findOrCreateCountry throws BadRequestException when name mismatch`() {
        val countryISO2 = "US"
        val savedName = "United States"
        val newName = "Different Name"
        countryRepository.save(CountryEntity(countryISO2 = countryISO2, countryName = savedName))

        val exception = assertThrows<BadRequestException> {
            underTest.findOrCreateCountry(countryISO2, newName)
        }

        assertThat(exception.message).isEqualTo(
            "Country name $newName does not match with $countryISO2"
        )
    }

    @Test
    fun `get returns country when exists`() {
        val countryISO2 = "US"
        val countryName = "United States"
        countryRepository.save(CountryEntity(countryISO2 = countryISO2, countryName = countryName))

        val result = underTest.get(countryISO2)

        assertThat(result).isNotNull()
        assertThat(result!!.countryISO2).isEqualTo(countryISO2)
        assertThat(result.countryName).isEqualTo(countryName)
    }

    @Test
    fun `get returns null when country does not exist`() {
        val result = underTest.get("XX")

        assertThat(result).isNull()
    }
}