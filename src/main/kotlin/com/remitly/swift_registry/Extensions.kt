package com.remitly.swift_registry

import com.remitly.swift_registry.domain.dto.BankBranchDto
import com.remitly.swift_registry.domain.dto.BankCreateRequest
import com.remitly.swift_registry.domain.dto.BankDto
import com.remitly.swift_registry.domain.dto.CountryDto
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
        branches = if (this.isHeadquarter) {
            this.branches?.map { it.toBankBranchDto() }
        } else {
            null
        }
    )
}


fun BankEntity.toBankBranchDto(): BankBranchDto {
    return BankBranchDto(
        address = this.address,
        bankName = this.bankName,
        countryISO2 = this.countryEntity.countryISO2,
        isHeadquarter = this.isHeadquarter,
        swiftCode = this.swiftCode
    )
}

fun BankCreateRequest.toBankEntity(countryEntity: CountryEntity, hq: BankEntity?) = BankEntity(
    swiftCode = this.swiftCode,
    address = this.address,
    bankName = this.bankName,
    countryEntity = countryEntity,
    isHeadquarter = this.headquarter,
    hq = hq,
    branches = null
)

fun CountryEntity.toCountryDto() : CountryDto {
    return CountryDto(
        countryISO2 = this.countryISO2,
        countryName = this.countryName,
        swiftCodes = this.swiftCodes.map { it.toBankBranchDto() },
    )
}