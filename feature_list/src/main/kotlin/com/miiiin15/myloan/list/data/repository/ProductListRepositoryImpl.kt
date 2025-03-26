package com.miiiin15.myloan.list.data.repository

import com.miiiin15.myloan.list.data.datasource.api.service.ProductListFirebaseService
import com.miiiin15.myloan.list.domain.model.Product
import com.miiiin15.myloan.list.domain.repository.ProductListRepository
import com.miiiin15.myloan.base.domain.result.Result
import com.miiiin15.myloan.list.data.datasource.api.model.LoanApplyStateEntityModel
import com.miiiin15.myloan.list.data.datasource.api.model.toDomain
import com.miiiin15.myloan.list.domain.model.ProductDetail
import com.miiiin15.myloan.list.domain.model.apply.LoanApplyState

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

    override suspend fun getProductDetail(productType: String): Result<ProductDetail> {
        return runCatching {
            val result = productListFirebaseService.getProductDetail(productType).productDetail
            result.toDomain()
        }.fold(
            onSuccess = { Result.Success(it) },
            onFailure = { Result.Failure(it) }
        )
    }

    override suspend fun submitLoanAgreement(loanApplyState: LoanApplyState): Result<Unit> {
        return runCatching {
            productListFirebaseService.submitLoanAgreement(
                LoanApplyStateEntityModel(
                    loanApplyState.productType,
                    loanApplyState.applicantId,
                    loanApplyState.accessToken,
                    loanApplyState.timeStamp,
                    loanApplyState.applyState,
                    loanApplyState.applyInfo
                )
            )
        }.fold(
            onSuccess = { Result.Success(it) },
            onFailure = { Result.Failure(it) }
        )
    }
}

