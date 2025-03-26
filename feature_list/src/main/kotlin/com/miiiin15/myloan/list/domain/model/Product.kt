package com.miiiin15.myloan.list.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Product(
    val group: String,           // 상품 그룹 (예: "LOAN")
    val productType: String,     // 상품 개별 타입 (예: "LOAN_CREDIT")
    val name: String,            // 상품 한글 이름 (예: "신용대출")
    val description: String,     // 상품 설명
    val rate: String,            // 대출 금리 (예: "7.4")
    val limit: String,           // 한도 금액 설명 (예: "최고 3천만원")
    val sale: Boolean            // 판매 여부 (true 고정)
)