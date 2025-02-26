package com.remitly.swift_registry.domain.entities

import jakarta.persistence.*

@Entity
@Table(name = "Bank")
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
    @JoinColumn(name = "hq_swift_code")
    var hq : BankEntity?,

    @OneToMany(mappedBy = "hq", fetch = FetchType.LAZY)
    val branches: List<BankEntity>?
)
