package com.miiiin15.myloan.app

import org.koin.dsl.module
import com.google.firebase.database.FirebaseDatabase

val appModule = module {
    // TODO : 파이어베이스 의존성 주입
//    single { FirebaseFirestore.getInstance() }
    single { FirebaseDatabase.getInstance() }
}
