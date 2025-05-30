package com.daromon.collective.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [CarEntity::class, ServiceRecordEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun carDao(): CarDao
    abstract fun serviceRecordDao(): ServiceRecordDao
}