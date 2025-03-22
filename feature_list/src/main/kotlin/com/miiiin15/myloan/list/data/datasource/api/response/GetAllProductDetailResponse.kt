package com.miiiin15.myloan.list.data.datasource.api.response

import com.google.firebase.database.DataSnapshot
import com.miiiin15.myloan.list.data.datasource.api.model.ProductListApiModel

data class GetProductDetailResponse(
    val productDetail: ProductListApiModel
) {
    constructor(snapshot: DataSnapshot) : this(
        productDetail = snapshot.getValue(ProductListApiModel::class.java) ?: ProductListApiModel()
    )
}