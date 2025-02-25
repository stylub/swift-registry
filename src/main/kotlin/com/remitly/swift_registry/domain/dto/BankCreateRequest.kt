package com.remitly.swift_registry.domain.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class BankCreateRequest(
    val address: String,
    val bankName: String,
    val countryISO2: String,
    val countryName: String,
    @JsonProperty("isHeadquarter")
    val isHeadquarter: Boolean,
    val swiftCode: String
)