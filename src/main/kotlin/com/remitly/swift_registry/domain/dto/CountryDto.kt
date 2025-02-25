package com.remitly.swift_registry.domain.dto

data class CountryDto(
    val countryISO2: String,
    val countryName: String,
    val swiftCodes: List<BankBranchDto>
)