package com.miiiin15.myloan.list.domain.usecase

import com.miiiin15.myloan.base.domain.result.Result
import com.miiiin15.myloan.list.domain.model.ProductDetail
import com.miiiin15.myloan.list.domain.repository.ProductListRepository

internal class GetProductDetailUseCase(
    private val productListRepository: ProductListRepository
) {
    suspend operator fun invoke(productType: String): Result<ProductDetail> {
        val result = productListRepository.getProductDetail(productType)
        return result
    }
}