package com.remitly.swift_registry.domain.entities

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
data class BankEntity(
    @Id
    val swiftCode : String,
    val address : String,
    val bankName : String,

    @ManyToOne
    @JoinColumn(name="countryISO2")
    val countryEntity : CountryEntity,

    val isHeadquarter : Boolean,

    @ManyToOne(cascade = [CascadeType.DETACH])
    @JoinColumn(name = "hq_swift_code", insertable = false, updatable = false)
    val hq : BankEntity?
)
