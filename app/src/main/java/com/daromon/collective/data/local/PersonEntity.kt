package com.daromon.collective.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "persons")
data class PersonEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val firstName: String,
    val lastName: String,
    val photoUri: String?,
    val phone: String?,
    val email: String?,
    val address: String?,
    val note: String?,
    val dateOfBirth: String?
)