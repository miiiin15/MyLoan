package com.miiiin15.myloan.base

import com.miiiin15.myloan.base.presentation.nav.NavManager
import org.koin.dsl.module

val baseModule = module {

    single { NavManager() }
}
