package com.xtech.xdevpocket.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val toolId: String,
    val timestamp: Long,
)
