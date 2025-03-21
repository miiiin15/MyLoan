package com.miiiin15.myloan.list.data.datasource.api.model

import com.miiiin15.myloan.list.domain.model.Product

data class ProductApiModel(
    val group: String = "",
    val productType: String = "",
    val name: String = "",
    val description: String = "",
    val rate: String = "",
    val limit: String = "",
    val sale: Boolean = false
)

// TODO: Room 용 Entity로 변환하는 메소드 추가

internal fun ProductApiModel.toDomain(): Product {
    return Product(
        group = group,
        productType = productType,
        name = name,
        description = description,
        rate = rate,
        limit = limit,
        sale = sale
    )
}