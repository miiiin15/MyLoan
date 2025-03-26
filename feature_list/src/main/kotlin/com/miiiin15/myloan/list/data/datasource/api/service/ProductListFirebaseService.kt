package com.miiiin15.myloan.list.data.datasource.api.service

import com.miiiin15.myloan.list.data.datasource.api.model.LoanApplyStateEntityModel
import com.miiiin15.myloan.list.data.datasource.api.response.GetAllProductListResponse
import com.miiiin15.myloan.list.data.datasource.api.response.GetProductDetailResponse
import com.miiiin15.myloan.list.domain.model.apply.LoanApplyState

interface ProductListFirebaseService {
    suspend fun getProductList(): GetAllProductListResponse
    suspend fun getProductDetail(productType: String): GetProductDetailResponse
    suspend fun submitLoanAgreement(loanApplyState: LoanApplyStateEntityModel): Unit
}