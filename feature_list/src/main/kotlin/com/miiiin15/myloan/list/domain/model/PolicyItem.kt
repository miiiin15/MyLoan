package com.miiiin15.myloan.list.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class PolicyItem(
    val id: String,
    val title: String,
    val required: Boolean,
    val files: List<String> = emptyList(),
    val options: List<String> = emptyList()
)