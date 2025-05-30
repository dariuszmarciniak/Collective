package com.daromon.collective.di

import android.content.Context
import androidx.room.Room
import com.daromon.collective.data.local.AppDatabase
import com.daromon.collective.data.local.CarDao
import com.daromon.collective.data.local.ServiceRecordDao
import com.daromon.collective.domain.repository.CarRepository
import com.daromon.collective.domain.repository.CarRepositoryImpl
import com.daromon.collective.domain.repository.ServiceRecordRepository
import com.daromon.collective.domain.repository.ServiceRecordRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "collective_room_db").build()

    @Provides
    fun provideCarDao(db: AppDatabase): CarDao = db.carDao()

    @Provides
    fun provideCarRepository(dao: CarDao): CarRepository = CarRepositoryImpl(dao)

    @Provides
    fun provideServiceRecordDao(db: AppDatabase) = db.serviceRecordDao()

    @Provides
    fun provideServiceRecordRepository(dao: ServiceRecordDao): ServiceRecordRepository =
        ServiceRecordRepositoryImpl(dao)
}