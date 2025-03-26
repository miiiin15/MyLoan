package com.miiiin15.myloan.app

import org.koin.dsl.module
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

val appModule = module {
    single { FirebaseFirestore.getInstance() }
    single { FirebaseDatabase.getInstance() }
}
