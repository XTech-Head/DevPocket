package com.xtech.xdevpocket.domain.utilities

import java.util.UUID

object UuidUtility {

    fun generate(): String = UUID.randomUUID().toString()

    fun generateMultiple(count: Int): List<String> {
        val safeCount = count.coerceIn(1, 100)
        return (1..safeCount).map { generate() }
    }
}
