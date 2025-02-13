package com.remitly.swift_registry.services

import com.remitly.swift_registry.domain.entities.CountryEntity
import com.remitly.swift_registry.repositories.CountryRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service


@Service
class CountryService(private val countryRepository: CountryRepository) {

    fun findOrCreateCountry(countryISO2: String, countryName: String): CountryEntity {
        return countryRepository.findByIdOrNull(countryISO2)
            ?: countryRepository.save(
                CountryEntity(
                    countryISO2 = countryISO2,
                    countryName = countryName
                )
            )
    }
}