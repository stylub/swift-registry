package com.remitly.swift_registry.domain.dto

data class BankBranchDto(
    val address: String,
    val bankName: String,
    val countryISO2: String,
    val isHeadquarter: Boolean,
    val swiftCode: String
)