package com.miiiin15.myloan.list.domain.model

import androidx.compose.runtime.Stable

@Stable
data class ProductDetail(
    val productName: String,
    val code:String,
    val rateRange: String,
    val limitAmount: String,
    val duration: String,
    val descriptionList: List<DetailItem>,
    val noticeList: List<DetailItem>
)
