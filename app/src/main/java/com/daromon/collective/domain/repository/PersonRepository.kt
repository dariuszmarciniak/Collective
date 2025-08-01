package com.daromon.collective.domain.repository

import com.daromon.collective.domain.model.Person
import kotlinx.coroutines.flow.Flow

interface PersonRepository {
    fun getAll(): Flow<List<Person>>
    suspend fun getById(id: Int): Person?
    suspend fun add(person: Person)
    suspend fun update(person: Person)
    suspend fun delete(person: Person)
}