package com.miiiin15.myloan.list.domain.model.apply

data class LoanApplyState(
    val productType: String = "",
    val applicantId : String = "",
    val accessToken: String = "",
    val timeStamp: Long = 0L,
    val applyState: String = "",
    val applyInfo: Any
)

