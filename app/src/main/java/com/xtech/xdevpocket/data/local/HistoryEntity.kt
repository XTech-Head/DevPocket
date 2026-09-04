package com.xtech.xdevpocket.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tool: String,
    val input: String,
    val output: String,
    val timestamp: Long,
)
