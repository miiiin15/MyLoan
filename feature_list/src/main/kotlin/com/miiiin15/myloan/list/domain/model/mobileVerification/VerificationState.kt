package com.miiiin15.myloan.list.domain.model.mobileVerification

data class UserInfo(
    val name: String? = null,
    val birthDate: String? = null,
    val residentialNumber: String? = null,
)

data class ContactInfo(
    val carrierCode: String? = null,
    val phoneNumber: String? = null,
)

data class VerificationInfo(
    val verificationCode: String? = null,
)