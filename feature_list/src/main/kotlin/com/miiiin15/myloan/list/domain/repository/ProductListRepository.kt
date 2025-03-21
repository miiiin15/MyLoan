package com.miiiin15.myloan.list.domain.repository

import com.miiiin15.myloan.list.domain.model.Product
import com.miiiin15.myloan.base.domain.result.Result

internal interface ProductListRepository {

     suspend fun getAllProductList(): Result<List<Product>>

}