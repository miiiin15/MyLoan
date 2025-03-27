package com.miiiin15.myloan.list.domain.model.apply

data class LoanApplyState(
    val applicantId : String = "",
    val productType: String = "",
    val applyNumber : String = "",
    val accessToken: String = "",
    val timeStamp: Long = 0L,
    val applyStep: String = "",
    val applyState: Any
)

