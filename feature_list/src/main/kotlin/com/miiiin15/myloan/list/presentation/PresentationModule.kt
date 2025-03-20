package com.miiiin15.myloan.list.presentation

import com.miiiin15.myloan.list.presentation.screen.list.ProductListViewmodel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val presentationModule = module {
    viewModelOf(::ProductListViewmodel)
}
