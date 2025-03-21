package com.miiiin15.myloan.list.domain.usecase

import com.miiiin15.myloan.list.domain.repository.ProductListRepository
import com.miiiin15.myloan.base.domain.result.Result
import com.miiiin15.myloan.list.domain.model.Product

internal class GetProductListUseCase(
    private val productListRepository: ProductListRepository
) {
    suspend operator fun invoke(): Result<List<Product>> {
        val result = productListRepository.getAllProductList()
        return result
    }
}