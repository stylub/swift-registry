package com.remitly.swift_registry.domain.dto

import com.fasterxml.jackson.annotation.JsonInclude

data class BankDto(
    val address: String,
    val bankName: String,
    val countryISO2: String,
    val countryName: String,
    val isHeadquarter: Boolean,
    val swiftCode: String,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val branches: List<BankBranchDto>?
)