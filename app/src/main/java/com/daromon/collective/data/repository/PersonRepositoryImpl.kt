package com.daromon.collective.data.repository

import com.daromon.collective.data.local.PersonDao
import com.daromon.collective.domain.model.Person
import com.daromon.collective.domain.model.toDomain
import com.daromon.collective.domain.model.toEntity
import com.daromon.collective.domain.repository.PersonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PersonRepositoryImpl(private val dao: PersonDao) : PersonRepository {
    override fun getAll(): Flow<List<Person>> =
        dao.getAll().map { list -> list.map { it.toDomain() } }

    override suspend fun getById(id: Int): Person? = dao.getById(id)?.toDomain()

    override suspend fun add(person: Person) {
        dao.insert(person.toEntity())
    }

    override suspend fun update(person: Person) {
        dao.update(person.toEntity())
    }

    override suspend fun delete(person: Person) {
        dao.delete(person.toEntity())
    }
}