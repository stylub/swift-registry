package com.remitly.swift_registry.controllers

import com.remitly.swift_registry.domain.dto.BankDto
import com.remitly.swift_registry.services.BankService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class BankControllers(private val bankService : BankService) {

    @PostMapping(path=["/v1/swift-codes"])
    fun createBank(@RequestBody bankDto: BankDto) : BankDto{
        return bankService.save(bankDto)
    }
}