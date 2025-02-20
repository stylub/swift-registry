package com.remitly.swift_registry.repositories

import com.remitly.swift_registry.domain.entities.BankEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface BankRepository : JpaRepository<BankEntity,String> {
    @Modifying
    @Query("UPDATE BankEntity b SET b.hq = :hq WHERE b.swiftCode LIKE :prefix AND b.isHeadquarter = false AND b.hq IS NULL")
    fun updateBranchesHQ(@Param("hq") hq: BankEntity, @Param("prefix") prefix: String) {
    }
}