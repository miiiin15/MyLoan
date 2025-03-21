package com.miiiin15.myloan.list.data.repository

import com.google.firebase.database.FirebaseDatabase
import com.miiiin15.myloan.list.data.datasource.api.response.GetAllProductListResponse
import com.miiiin15.myloan.list.data.datasource.api.service.ProductListFirebaseService
import kotlinx.coroutines.tasks.await

internal class ProductListFirebaseServiceImpl(
    private val firebaseDatabase: FirebaseDatabase
) : ProductListFirebaseService {

    override suspend fun getProductList(): GetAllProductListResponse {
        try {
            val productListRef = firebaseDatabase.getReference("product_list")
            val snapshot = productListRef.get().await()
            return GetAllProductListResponse(snapshot)
        } catch (e: Exception) {
            throw e
        }
    }
}