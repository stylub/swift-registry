package com.remitly.swift_registry.domain.entities

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
data class CountryEntity(
    @Id
    val countryISO2 : String,
    val countryName : String,
    @OneToMany(mappedBy = "countryEntity", fetch = FetchType.LAZY)
    val swiftCodes: List<BankEntity> = emptyList()
)