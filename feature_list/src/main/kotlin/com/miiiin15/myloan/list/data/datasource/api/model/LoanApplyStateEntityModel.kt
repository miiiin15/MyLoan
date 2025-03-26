package com.miiiin15.myloan.list.data.datasource.api.model

import com.miiiin15.myloan.list.domain.model.apply.LoanApplyState

data class LoanApplyStateEntityModel(
    val productType: String,
    val applicantId: String,
    val accessToken: String,
    val timeStamp: Long,
    val applyState: String,
    val applyInfo: Any
)

fun LoanApplyStateEntityModel.toDomain() = LoanApplyState(
    productType = productType,
    applicantId = applicantId,
    accessToken = accessToken,
    timeStamp = timeStamp,
    applyState = applyState,
    applyInfo = applyInfo
)