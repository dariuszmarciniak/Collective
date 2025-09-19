package com.daromon.collective.ui.features.person

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daromon.collective.domain.model.Person
import com.daromon.collective.domain.repository.PersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(private val repository: PersonRepository) : ViewModel() {
    val persons: StateFlow<List<Person>> = repository.getAll()
        .stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), emptyList())

    private val _selectedPerson = MutableStateFlow<Person?>(null)
    val selectedPerson: StateFlow<Person?> = _selectedPerson

    fun loadPerson(id: Int) {
        viewModelScope.launch {
            _selectedPerson.value = repository.getById(id)
        }
    }

    fun add(person: Person) {
        viewModelScope.launch { repository.add(person) }
    }

    fun update(person: Person) {
        viewModelScope.launch { repository.update(person) }
    }

    fun delete(person: Person) {
        viewModelScope.launch { repository.delete(person) }
    }
}