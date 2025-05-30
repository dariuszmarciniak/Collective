package com.daromon.collective.domain.usecase

import com.daromon.collective.domain.model.ServiceRecord
import com.daromon.collective.domain.repository.ServiceRecordRepository
import javax.inject.Inject

class DeleteServiceRecordUseCase @Inject constructor(private val repo: ServiceRecordRepository) {
    suspend operator fun invoke(serviceRecord: ServiceRecord) = repo.delete(serviceRecord)
}