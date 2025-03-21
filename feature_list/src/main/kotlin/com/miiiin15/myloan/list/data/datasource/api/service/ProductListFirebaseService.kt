package com.miiiin15.myloan.list.data.datasource.api.service

import com.miiiin15.myloan.list.data.datasource.api.response.GetAllProductListResponse

interface ProductListFirebaseService {
    suspend fun getProductList(): GetAllProductListResponse
}