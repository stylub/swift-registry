package com.remitly.swift_registry.controllers

import com.remitly.swift_registry.domain.dto.ApiResponse
import com.remitly.swift_registry.domain.dto.BankCreateRequest
import com.remitly.swift_registry.domain.dto.BankDto
import com.remitly.swift_registry.services.BankService
import com.remitly.swift_registry.toBankDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/swift-codes/")
class BankControllers(private val bankService : BankService) {

    @PostMapping
    fun createBank(@RequestBody bankCreateRequest: BankCreateRequest): ResponseEntity<ApiResponse> {
        try {
            bankService.save(bankCreateRequest)
            return ResponseEntity(
                ApiResponse("Bank created successfully"),
                HttpStatus.CREATED
            )
        } catch (e: Exception) {
            return ResponseEntity(
                ApiResponse("Error: ${e.message}"),
                HttpStatus.BAD_REQUEST
            )
        }
    }

    @GetMapping(path = ["/{swift-code}"])
    fun retrieveSingleBank(@PathVariable("swift-code") swiftCode: String): ResponseEntity<BankDto> {
        val foundBank = bankService.getOneBySwiftCode(swiftCode)?.toBankDto()
        return foundBank?.let {
            ResponseEntity(it,HttpStatus.OK)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @DeleteMapping(path = ["/{swift-code}"])
    fun deleteBank(@PathVariable("swift-code") swiftCode: String): ResponseEntity<ApiResponse> {
        try {
            bankService.delete(swiftCode)
            return ResponseEntity(
                ApiResponse("Bank deleted successfully"),
                HttpStatus.OK
            )
        } catch (e: Exception) {
            return ResponseEntity(
                ApiResponse("Error: ${e.message}"),
                HttpStatus.BAD_REQUEST
            )
        }
    }
}