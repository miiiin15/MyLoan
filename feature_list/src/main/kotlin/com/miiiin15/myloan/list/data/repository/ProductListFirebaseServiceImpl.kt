package com.miiiin15.myloan.list.data.repository

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.miiiin15.myloan.base.data.firebase.setDocument
import com.miiiin15.myloan.list.data.datasource.api.model.LoanApplyStateEntityModel
import com.miiiin15.myloan.list.data.datasource.api.response.GetAllProductListResponse
import com.miiiin15.myloan.list.data.datasource.api.response.GetProductDetailResponse
import com.miiiin15.myloan.list.data.datasource.api.service.ProductListFirebaseService
import kotlinx.coroutines.tasks.await

internal class ProductListFirebaseServiceImpl(
    private val firestore: FirebaseFirestore,
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

    override suspend fun getProductDetail(productType: String): GetProductDetailResponse {
        try {
            val productDetailRef = firebaseDatabase.getReference("product_detail/$productType")
            val snapshot = productDetailRef.get().await()
            return GetProductDetailResponse(snapshot)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun submitLoanAgreement(loanApplyState: LoanApplyStateEntityModel): Unit {
        return firestore.setDocument(
            collectionPath = "loan_applications",
            documentId = loanApplyState.applicantId,
            data = loanApplyState,
            errorLabel = "submitLoanAgreement"
        )
    }
}