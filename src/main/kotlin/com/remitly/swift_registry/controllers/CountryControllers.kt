package com.remitly.swift_registry.controllers

import com.remitly.swift_registry.domain.dto.CountryDto
import com.remitly.swift_registry.services.CountryService
import com.remitly.swift_registry.toCountryDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController



@RestController
@RequestMapping("/v1/swift-codes/country/")
class CountryControllers(private val countryService: CountryService) {

    @GetMapping(path = ["/{countryISO2code}"])
    fun getBanksByCountry(@PathVariable("countryISO2code") countryISO2: String): ResponseEntity<CountryDto>{
        val foundCountry = countryService.get(countryISO2)?.toCountryDto()
        return foundCountry?.let {
            ResponseEntity(it, HttpStatus.OK)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

}