package com.miiiin15.myloan.list.domain.model.mobileVerification

data class AuthTransaction(
    val transactionId: String,
    val phoneNumber: String,
    val token: String,
    val verificationPurpose: VerificationPurposeName
)



enum class VerificationPurposeName {
    LOGIN,
    SIGNUP,
    PASSWORD_RESET,
    LOAN_APPLY,
    // 필요한 값 추가
}