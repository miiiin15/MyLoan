package com.miiiin15.myloan.list.domain.model

data class ApplyInfo(
    // 대출 신청 정보
    var applicantId: String = "",
    var productType: String = "",
    var currentStep : String="",
    var applyNumber: String = "",
    // 신청자 정보
    var userName:String = "",
    var userPhoneNumber: String = "",
    var userResidentialNumber : String = "",
)
