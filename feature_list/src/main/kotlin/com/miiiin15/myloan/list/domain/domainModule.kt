package com.miiiin15.myloan.list.domain

import com.miiiin15.myloan.list.domain.usecase.GetApplicantUseCase
import com.miiiin15.myloan.list.domain.usecase.SetApplicantUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val domainModule = module {
    singleOf(::GetApplicantUseCase)
    singleOf(::SetApplicantUseCase)
}