package com.remitly.swift_registry

fun deriveHqSwiftCode(branchSwiftCode: String): String {
    require(branchSwiftCode.length >= 8) { "Invalid branch SwiftCode" }
    return branchSwiftCode.take(8) + "XXX"
}