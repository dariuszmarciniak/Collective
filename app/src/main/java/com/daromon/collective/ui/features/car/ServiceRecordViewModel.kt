package com.daromon.collective.ui.features.car

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daromon.collective.domain.model.ServiceRecord
import com.daromon.collective.domain.usecase.AddServiceRecordUseCase
import com.daromon.collective.domain.usecase.DeleteServiceRecordUseCase
import com.daromon.collective.domain.usecase.GetServiceRecordsForCarUseCase
import com.daromon.collective.domain.usecase.UpdateServiceRecordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServiceRecordViewModel @Inject constructor(
    private val getRecords: GetServiceRecordsForCarUseCase,
    private val addRecord: AddServiceRecordUseCase,
    private val updateRecord: UpdateServiceRecordUseCase,
    private val deleteRecord: DeleteServiceRecordUseCase
) : ViewModel() {

    private val _records = MutableStateFlow<List<ServiceRecord>>(emptyList())
    val records: StateFlow<List<ServiceRecord>> = _records.asStateFlow()

    private var currentJob: Job? = null

    fun loadRecords(carId: Int) {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            getRecords(carId).collectLatest { _records.value = it }
        }
    }

    fun add(record: ServiceRecord) {
        viewModelScope.launch {
            addRecord(record)
            loadRecords(record.carId)
        }
    }

    fun update(record: ServiceRecord) {
        viewModelScope.launch {
            updateRecord(record)
            loadRecords(record.carId)
        }
    }

    fun delete(record: ServiceRecord) {
        viewModelScope.launch {
            deleteRecord(record)
            loadRecords(record.carId)
        }
    }
}