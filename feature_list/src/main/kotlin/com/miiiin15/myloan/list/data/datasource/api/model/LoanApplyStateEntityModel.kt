package com.miiiin15.myloan.list.data.datasource.api.model

import com.miiiin15.myloan.list.domain.model.apply.LoanApplyState

data class LoanApplyStateEntityModel(
    val applicantId: String,
    val productType: String,
    val applyNumber: String,
    val accessToken: String,
    val timeStamp: Long,
    val applyStep: String,
    val applyState: Any
)

fun LoanApplyStateEntityModel.toDomain() = LoanApplyState(
    applicantId = applicantId,
    productType = productType,
    applyNumber = applyNumber,
    accessToken = accessToken,
    timeStamp = timeStamp,
    applyStep = applyStep,
    applyState = applyState
)