package com.remitly.swift_registry

import com.remitly.swift_registry.domain.dto.BankDto
import com.remitly.swift_registry.domain.entities.BankEntity
import com.remitly.swift_registry.domain.entities.CountryEntity

fun BankEntity.toBankDto(branches: List<BankEntity>? = null): BankDto {
    return BankDto(
        address = this.address,
        bankName = this.bankName,
        countryISO2 = this.countryEntity.countryISO2,
        countryName = this.countryEntity.countryName,
        isHeadquarter = this.isHeadquarter,
        swiftCode = this.swiftCode,
        branches = branches?.map { branchEntity ->
            branchEntity.toBankDto(branches = null)
        }
    )
}

fun BankDto.toBankEntity(countryEntity: CountryEntity, hq: BankEntity? = null) = BankEntity(
    swiftCode = this.swiftCode,
    address = this.address,
    bankName = this.bankName,
    countryEntity = countryEntity,
    isHeadquarter = this.isHeadquarter,
    hq = hq
)