package com.remitly.swift_registry.repositories

import com.remitly.swift_registry.domain.entities.BankEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BankRepository : JpaRepository<BankEntity,String>