package com.miiiin15.myloan.app

import android.app.Application
import com.google.android.material.color.DynamicColors
import com.miiiin15.myloan.BuildConfig
import com.miiiin15.myloan.base.baseModule
import com.miiiin15.myloan.list.featureListModules
import com.miiiin15.myloan.result.featureResultModules

import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import timber.log.Timber

class MyLoanApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin()
        initTimber()
        initDynamicColorScheme()
    }

    private fun initDynamicColorScheme() {
        // Apply dynamic colors to all Activities, Fragments, Views
        // (Material 3 library helper class)
        DynamicColors.applyToActivitiesIfAvailable(this)
    }

    private fun initKoin() {
        GlobalContext.startKoin {
            androidLogger()
            androidContext(this@MyLoanApplication)

            modules(appModule)
            modules(baseModule)
            // TODO : feature 모듈 주입
            modules(featureListModules)
            modules(featureResultModules)

        }
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
