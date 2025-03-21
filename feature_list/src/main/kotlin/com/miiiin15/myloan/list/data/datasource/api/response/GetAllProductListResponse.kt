package com.miiiin15.myloan.list.data.datasource.api.response

import com.google.firebase.database.DataSnapshot
import com.miiiin15.myloan.list.data.datasource.api.model.ProductApiModel

data class GetAllProductListResponse(
    val productList: List<ProductApiModel>
) {
    constructor(snapshot: DataSnapshot) : this(
        productList = snapshot.children.mapNotNull { it.getValue(ProductApiModel::class.java) }
    )
}
