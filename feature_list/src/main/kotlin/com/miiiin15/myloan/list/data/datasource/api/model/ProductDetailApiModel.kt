package com.miiiin15.myloan.list.data.datasource.api.model

import com.miiiin15.myloan.list.domain.model.DetailItem
import com.miiiin15.myloan.list.domain.model.ProductDetail

data class ProductDetailApiModel(
    val productName: String = "",
    val code: String = "",
    val rateRange: String = "",
    val limitAmount: String = "",
    val duration: String = "",
    val descriptionList: List<DetailItemApiModel> = emptyList(),
    val noticeList: List<DetailItemApiModel> = emptyList()
)

data class DetailItemApiModel(
    val key: String = "",
    val value: String = ""
)

internal fun ProductDetailApiModel.toDomain(): ProductDetail {
    return ProductDetail(
        productName = productName,
        code = code,
        rateRange = rateRange,
        limitAmount = limitAmount,
        duration = duration,
        descriptionList = descriptionList.map { DetailItem(it.key, it.value) },
        noticeList = noticeList.map { DetailItem(it.key, it.value) }
    )
}