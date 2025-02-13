package com.remitly.swift_registry.services

import com.remitly.swift_registry.domain.dto.BankCreateRequest
import com.remitly.swift_registry.domain.dto.BankDto
import com.remitly.swift_registry.repositories.BankRepository
import org.springframework.stereotype.Service

import com.remitly.swift_registry.toBankDto
import com.remitly.swift_registry.toBankEntity

@Service
class BankService(
    private val bankRepository: BankRepository,
    private val countryService: CountryService
) {
    fun save(bankCreateRequest: BankCreateRequest): BankDto {
        val countryEntity = countryService.findOrCreateCountry(
            bankCreateRequest.countryISO2,
            bankCreateRequest.countryName
        )

        val bankEntity = bankCreateRequest.toBankEntity(countryEntity)

        return bankRepository.save(bankEntity).toBankDto()
    }
}