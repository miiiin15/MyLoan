package com.miiiin15.myloan.list.domain.repository


import com.miiiin15.myloan.list.domain.model.ApplyInfo
import com.miiiin15.myloan.list.domain.model.ApplyUser

interface ApplyInfoRepository {
    fun setApplyAllInfo(info: ApplyInfo)
    fun setApplyUser(user: ApplyUser)
    fun setApplyInfo(keyword: String, value: String)
    fun getApplyAllInfo(): ApplyInfo?
    fun getApplyUser(): ApplyUser?
    fun getApplyInfo(keyword: String): String
    fun clearApplyInfo()
    fun clearApplyUser()
    fun clearAll()
}