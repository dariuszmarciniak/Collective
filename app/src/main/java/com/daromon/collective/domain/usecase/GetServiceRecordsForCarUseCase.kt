package com.daromon.collective.domain.usecase

import com.daromon.collective.domain.repository.ServiceRecordRepository
import javax.inject.Inject

class GetServiceRecordsForCarUseCase @Inject constructor(
    private val repo: ServiceRecordRepository
) {
    operator fun invoke(carId: Int) = repo.getRecordsForCar(carId)
}