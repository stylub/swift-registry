package com.remitly.swift_registry.services
import com.remitly.swift_registry.domain.dto.BankCreateRequest
import com.remitly.swift_registry.domain.dto.BankDto
import com.remitly.swift_registry.domain.entities.BankEntity
import com.remitly.swift_registry.repositories.BankRepository
import org.springframework.stereotype.Service

import com.remitly.swift_registry.toBankDto
import com.remitly.swift_registry.toBankEntity
import org.apache.coyote.BadRequestException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional

@Service
class BankService(
    private val bankRepository: BankRepository,
    private val countryService: CountryService
) {

    @Transactional
    fun save(request: BankCreateRequest): BankDto {
        val countryEntity = countryService.findOrCreateCountry(
            request.countryISO2,
            request.countryName
        )
        validateBank(request)

        val bankEntity : BankEntity
        val savedEntity : BankEntity

        if(request.headquarter){
            bankEntity = request.toBankEntity(countryEntity, hq = null)
            savedEntity = bankRepository.save(bankEntity)

            val prefix = request.swiftCode.substring(0, 8)
            bankRepository.updateBranchesHQ(savedEntity, "$prefix%")
        }else{
            val hq = linkBranchToExistingHQ(request)
            bankEntity = request.toBankEntity(countryEntity, hq = hq)
            savedEntity = bankRepository.save(bankEntity)
        }
        return savedEntity.toBankDto()
    }

    private fun validateBank(bank: BankCreateRequest) {
        val isHqFromSwift = bank.swiftCode.endsWith("XXX")
        if (bank.headquarter != isHqFromSwift) {
            throw IllegalArgumentException("isHeadquarter does not match SWIFT code suffix")
        }
    }

    private fun linkBranchToExistingHQ(branch: BankCreateRequest): BankEntity? {
        val prefix = branch.swiftCode.substring(0, 8)
        val hqSwift = "${prefix}XXX"
        return bankRepository.findByIdOrNull(hqSwift)
    }



    fun getOneBySwiftCode(swiftCode : String) : BankEntity? {
        return bankRepository.findByIdOrNull(swiftCode)
    }
}