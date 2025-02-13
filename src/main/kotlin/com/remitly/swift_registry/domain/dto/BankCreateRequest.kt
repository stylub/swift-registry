package com.remitly.swift_registry.domain.dto

data class BankCreateRequest(
    val address: String,
    val bankName: String,
    val countryISO2: String,
    val countryName: String,
    val isHeadquarter: Boolean,
    val swiftCode: String
)