package com.miiiin15.myloan.list.data.repository

import com.miiiin15.myloan.list.data.datasource.api.service.ProductListFirebaseService
import com.miiiin15.myloan.list.domain.model.Product
import com.miiiin15.myloan.list.domain.repository.ProductListRepository
import com.miiiin15.myloan.base.domain.result.Result
import com.miiiin15.myloan.list.data.datasource.api.model.toDomain

internal class ProductListRepositoryImpl(
    private val productListFirebaseService: ProductListFirebaseService,
) : ProductListRepository {
    override suspend fun getAllProductList(): Result<List<Product>> {
        return runCatching {
            val result = productListFirebaseService.getProductList().productList
            result.map { it.toDomain() }
        }.fold(
            onSuccess = { Result.Success(it) },
            onFailure = { Result.Failure(it) }
        )
    }
}

