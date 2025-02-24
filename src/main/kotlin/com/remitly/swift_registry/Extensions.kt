package com.remitly.swift_registry

import com.remitly.swift_registry.domain.dto.BankCreateRequest
import com.remitly.swift_registry.domain.dto.BankDto
import com.remitly.swift_registry.domain.entities.BankEntity
import com.remitly.swift_registry.domain.entities.CountryEntity

fun BankEntity.toBankDto(): BankDto {
    return BankDto(
        address = this.address,
        bankName = this.bankName,
        countryISO2 = this.countryEntity.countryISO2,
        countryName = this.countryEntity.countryName,
        isHeadquarter = this.isHeadquarter,
        swiftCode = this.swiftCode,
        branches = this.branches.takeIf { it.isNotEmpty() }?.map { branchEntity ->
            branchEntity.toBankDto()
        }
    )
}

fun BankCreateRequest.toBankEntity(countryEntity: CountryEntity, hq: BankEntity?) = BankEntity(
    swiftCode = this.swiftCode,
    address = this.address,
    bankName = this.bankName,
    countryEntity = countryEntity,
    isHeadquarter = this.headquarter,
    hq = hq
)

fun BankDto.toBankEntity(countryEntity: CountryEntity, hq: BankEntity? = null) = BankEntity(
    swiftCode = this.swiftCode,
    address = this.address,
    bankName = this.bankName,
    countryEntity = countryEntity,
    isHeadquarter = this.isHeadquarter,
    hq = hq
)

fun BankEntity.toBankDto(): BankDto {
    return BankDto(
        address = this.address,
        bankName = this.bankName,
        countryISO2 = this.countryEntity.countryISO2,
        countryName = this.countryEntity.countryName,
        isHeadquarter = this.isHeadquarter,
        swiftCode = this.swiftCode,
        branches = null
    )
}