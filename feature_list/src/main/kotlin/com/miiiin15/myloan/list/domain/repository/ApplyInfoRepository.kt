package com.miiiin15.myloan.list.domain.repository


import com.miiiin15.myloan.list.domain.model.ApplyInfo

interface ApplyInfoRepository {
    fun setApplyAllInfo(info: ApplyInfo)
    fun setApplyInfo(keyword: String, value: String)
    fun getApplyAllInfo(): ApplyInfo?
    fun getApplyInfo(keyword: String): String
    fun clear()
}