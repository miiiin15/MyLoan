package com.miiiin15.myloan.list.data.datasource.api.response

import com.google.firebase.database.DataSnapshot
import com.miiiin15.myloan.list.data.datasource.api.model.ProductDetailApiModel

data class GetProductDetailResponse(
    val productDetail: ProductDetailApiModel
) {
    constructor(snapshot: DataSnapshot) : this(
        productDetail = snapshot.getValue(ProductDetailApiModel::class.java) ?: ProductDetailApiModel()
    )
}