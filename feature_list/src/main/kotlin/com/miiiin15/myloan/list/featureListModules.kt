package com.miiiin15.myloan.list

import com.miiiin15.myloan.list.data.dataModule
import com.miiiin15.myloan.list.domain.domainModule
import com.miiiin15.myloan.list.presentation.presentationModule

val featureListModules = listOf(
    domainModule,
    dataModule,
    presentationModule
)