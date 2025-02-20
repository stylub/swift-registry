package com.remitly.swift_registry

import com.remitly.swift_registry.domain.dto.BankCreateRequest

fun testBankCreateRequestA(swiftCode : String) : BankCreateRequest{
    return BankCreateRequest(
        address = "HQ Address 123",
        bankName = "GlobalBank HQ",
        countryISO2 = "US",
        countryName = "United States",
        headquarter = true,
        swiftCode = swiftCode
    )
}