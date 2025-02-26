package com.remitly.swift_registry

import com.remitly.swift_registry.domain.dto.BankCreateRequest
import com.remitly.swift_registry.domain.entities.BankEntity
import com.remitly.swift_registry.domain.entities.CountryEntity

fun testBankCreateRequestHQA(swiftCode : String) : BankCreateRequest{
    return BankCreateRequest(
        address = "HQ Address 123",
        bankName = "GlobalBank HQ",
        countryISO2 = "US",
        countryName = "United States",
        isHeadquarter = true,
        swiftCode = swiftCode
    )
}

fun testBankCreateRequestBranchA(swiftCode : String) : BankCreateRequest{
    return BankCreateRequest(
        address = "Branch Address 123",
        bankName = "GlobalBank Branch",
        countryISO2 = "US",
        countryName = "United States",
        isHeadquarter = false,
        swiftCode = swiftCode
    )
}

fun testCountryEntityUS() : CountryEntity {
    return  CountryEntity(
        countryISO2 = "US",
        countryName = "United States",
    )
}

fun testBankEntityA(swiftCode: String) : BankEntity {

    return testBankCreateRequestHQA(swiftCode).toBankEntity(countryEntity = testCountryEntityUS(), hq = null)
}


