package com.remitly.swift_registry.domain.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class CountryEntity(
    @Id
    val countryISO2 : String,
    val countryName : String
)