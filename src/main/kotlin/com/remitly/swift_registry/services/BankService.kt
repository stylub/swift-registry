package com.remitly.swift_registry.services

import com.remitly.swift_registry.deriveHqSwiftCode
import com.remitly.swift_registry.domain.dto.BankCreateRequest
import com.remitly.swift_registry.domain.dto.BankDto
import com.remitly.swift_registry.domain.entities.BankEntity
import com.remitly.swift_registry.repositories.BankRepository
import org.springframework.stereotype.Service

import com.remitly.swift_registry.toBankDto
import com.remitly.swift_registry.toBankEntity
import org.apache.coyote.BadRequestException

@Service
class BankService(
    private val bankRepository: BankRepository,
    private val countryService: CountryService
) {
    fun save(request: BankCreateRequest): BankDto {
        val countryEntity = countryService.findOrCreateCountry(
            request.countryISO2,
            request.countryName
        )

        var hq: BankEntity? = null
        if (!request.headquarter) {
            val hqSwiftCode = deriveHqSwiftCode(request.swiftCode)
            hq = bankRepository.findById(hqSwiftCode)
                .orElse(null)
                ?.takeIf { it.isHeadquarter }

            if (hq == null) {
                throw BadRequestException("Headquarter bank not found for branch bank with SWIFT code: ${request.swiftCode}")
            }
        }

        val bankEntity = request.toBankEntity(countryEntity,hq)

        return bankRepository.save(bankEntity).toBankDto()
    }
}