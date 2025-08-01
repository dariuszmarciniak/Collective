package com.daromon.collective.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [CarEntity::class, ServiceRecordEntity::class, PersonEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun carDao(): CarDao
    abstract fun serviceRecordDao(): ServiceRecordDao
    abstract fun personDao(): PersonDao
}