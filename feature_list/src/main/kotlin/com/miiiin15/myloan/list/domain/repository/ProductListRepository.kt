package com.miiiin15.myloan.list.domain.repository

import com.miiiin15.myloan.list.domain.model.Product
import com.miiiin15.myloan.base.domain.result.Result
import com.miiiin15.myloan.list.domain.model.ProductDetail
import com.miiiin15.myloan.list.domain.model.apply.LoanApplyState

interface ProductListRepository {
    suspend fun getAllProductList(): Result<List<Product>>
    suspend fun getProductDetail(productType: String): Result<ProductDetail>
    suspend fun submitLoanAgreement(loanApplyState: LoanApplyState): Result<Unit>
}