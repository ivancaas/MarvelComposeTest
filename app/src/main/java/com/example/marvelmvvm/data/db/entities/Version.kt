package com.example.marvelmvvm.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Version(
    @PrimaryKey val id: Int = 1,
    val version: Int,
    val alreadyScrapedToday: Boolean
)