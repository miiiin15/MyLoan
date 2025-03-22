package com.miiiin15.myloan.list.data.datasource.api.service

import com.miiiin15.myloan.list.data.datasource.api.response.GetAllProductListResponse
import com.miiiin15.myloan.list.data.datasource.api.response.GetProductDetailResponse

interface ProductListFirebaseService {
    suspend fun getProductList(): GetAllProductListResponse
    suspend fun getProductDetail(productType: String): GetProductDetailResponse
}