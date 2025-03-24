package com.miiiin15.myloan.list.domain.model.mobileVerification

data class AuthConfirmation(
    val transactionId: String,
    val phoneNumber: String,
    val token: String,
    val verificationPurpose: VerificationPurposeName
)