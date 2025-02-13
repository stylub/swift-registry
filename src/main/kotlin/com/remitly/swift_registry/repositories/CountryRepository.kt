package com.remitly.swift_registry.repositories

import com.remitly.swift_registry.domain.entities.CountryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CountryRepository : JpaRepository<CountryEntity,String>