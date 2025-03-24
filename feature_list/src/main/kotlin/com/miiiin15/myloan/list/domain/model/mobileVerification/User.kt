package com.miiiin15.myloan.list.domain.model.mobileVerification

data class User(
    val id: String,
    val seq: String,
    val name: String,
    val phoneNumber: String,
    val residenceNumberMasked: String
)
