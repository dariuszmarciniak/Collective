package com.daromon.collective.domain.model

data class Person(
    val id: Int = 0,
    val firstName: String,
    val lastName: String,
    val photoUri: String?,
    val phone: String?,
    val email: String?,
    val address: String?,
    val note: String?,
    val dateOfBirth: String?
)
