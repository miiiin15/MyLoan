package com.miiiin15.myloan.list.data

import androidx.room.Room
import com.miiiin15.myloan.list.data.datasource.database.ApplicantDatabase
import com.miiiin15.myloan.list.data.repository.ApplicantRepositoryImpl
import com.miiiin15.myloan.list.domain.repository.ApplicantRepository
import org.koin.dsl.module

internal val dataModule = module {
    single<ApplicantRepository> { ApplicantRepositoryImpl(get()) }

    single {
        Room.databaseBuilder(
            get(),
            ApplicantDatabase::class.java,
            "Applicant.db",
        ).build()
    }

    single { get<ApplicantDatabase>().applicant() }
}