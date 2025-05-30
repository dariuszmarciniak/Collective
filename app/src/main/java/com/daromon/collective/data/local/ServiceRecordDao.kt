package com.daromon.collective.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceRecordDao {
    @Query("SELECT * FROM service_records WHERE carId = :carId ORDER BY date DESC")
    fun getRecordsForCar(carId: Int): Flow<List<ServiceRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: ServiceRecordEntity)

    @Delete
    suspend fun delete(record: ServiceRecordEntity)

    @Update
    suspend fun update(record: ServiceRecordEntity)
}