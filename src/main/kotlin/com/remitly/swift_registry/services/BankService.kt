package com.remitly.swift_registry.services

import com.remitly.swift_registry.domain.dto.BankDto
import com.remitly.swift_registry.domain.entities.BankEntity
import com.remitly.swift_registry.repositories.BankRepository
import org.springframework.stereotype.Service

import com.remitly.swift_registry.toBankDto
import com.remitly.swift_registry.toBankEntity

@Service
class BankService(
    private val bankRepository: BankRepository,
    private val countryService: CountryService
) {
    fun save(bankDto: BankDto): BankDto {
        val countryEntity = countryService.findOrCreateCountry(
            bankDto.countryISO2,
            bankDto.countryName
        )

        val bankEntity = bankDto.toBankEntity(countryEntity)

        return bankRepository.save(bankEntity).toBankDto()
    }
}