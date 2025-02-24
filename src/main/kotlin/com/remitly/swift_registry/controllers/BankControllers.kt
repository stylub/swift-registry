package com.remitly.swift_registry.controllers

import com.remitly.swift_registry.domain.dto.BankCreateRequest
import com.remitly.swift_registry.domain.dto.BankDto
import com.remitly.swift_registry.services.BankService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/swift-codes/")
class BankControllers(private val bankService : BankService) {

    @PostMapping
    fun createBank(@RequestBody bankCreateRequest: BankCreateRequest) : ResponseEntity<BankDto>{
        val createdBank = bankService.save(bankCreateRequest)
        return ResponseEntity(createdBank,HttpStatus.CREATED)
    }

    @GetMapping(path = ["/{swift-code}"])
    fun retrieveSingleBank(@PathVariable("swift-code") swiftCode: String): BankDto {
        return bankService.getOneBySwiftCode(swiftCode)
    }
}