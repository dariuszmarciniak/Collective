package com.daromon.collective.domain.model

import com.daromon.collective.data.local.PersonEntity

fun PersonEntity.toDomain(): Person = Person(
    id = id,
    firstName = firstName,
    lastName = lastName,
    photoUri = photoUri,
    phone = phone,
    email = email,
    address = address,
    note = note,
    dateOfBirth = dateOfBirth
)

fun Person.toEntity(): PersonEntity = PersonEntity(
    id = id,
    firstName = firstName,
    lastName = lastName,
    photoUri = photoUri,
    phone = phone,
    email = email,
    address = address,
    note = note,
    dateOfBirth = dateOfBirth
)