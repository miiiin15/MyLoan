package com.miiiin15.myloan.list.data

import androidx.room.Room
import com.miiiin15.myloan.list.data.datasource.api.service.ProductListFirebaseService
import com.miiiin15.myloan.list.data.datasource.database.ApplicantDatabase
import com.miiiin15.myloan.list.data.repository.ApplicantRepositoryImpl
import com.miiiin15.myloan.list.data.repository.ProductListFirebaseServiceImpl
import com.miiiin15.myloan.list.data.repository.ProductListRepositoryImpl
import com.miiiin15.myloan.list.domain.repository.ApplicantRepository
import com.miiiin15.myloan.list.domain.repository.ProductListRepository
import org.koin.dsl.module

internal val dataModule = module {
    single<ProductListFirebaseService> { ProductListFirebaseServiceImpl(get()) }

    single<ProductListRepository> { ProductListRepositoryImpl(get()) }
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