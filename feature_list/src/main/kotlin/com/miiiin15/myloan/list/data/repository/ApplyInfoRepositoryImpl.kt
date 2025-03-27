package com.miiiin15.myloan.list.data.repository

import com.miiiin15.myloan.list.domain.model.ApplyInfo
import com.miiiin15.myloan.list.domain.repository.ApplyInfoRepository
import timber.log.Timber

class ApplyInfoRepositoryImpl : ApplyInfoRepository {
    private var applyInfo: ApplyInfo? = null
    private var applicantId: String? = null

    override fun setApplyAllInfo(info: ApplyInfo) {
        Timber.d("👇대출 신청 정보 저장: $info")
        applyInfo = info
    }

    override fun getApplyAllInfo(): ApplyInfo? = applyInfo

    override fun setApplyInfo(keyword: String, value: String) {
        Timber.d("👇 대출 신청 정보 저장: $keyword = $value")
        if (applyInfo == null) {
            applyInfo = ApplyInfo()
        }
        when (keyword) {
            "applicantId" -> applicantId = value
            "productType" -> applyInfo?.productType = value
            "currentStep" -> applyInfo?.currentStep = value
            "applyNumber" -> applyInfo?.applyNumber = value
            "userName" -> applyInfo?.userName = value
            "userPhoneNumber" -> applyInfo?.userPhoneNumber = value
            "userResidentialNumber" -> applyInfo?.userResidentialNumber = value
            else -> Timber.w("❌ 알 수 없는 키워드: $keyword")
        }
    }


    override fun getApplyInfo(keyword: String): String {
        Timber.d("👀 대출 신청 정보 조회: $keyword")
        return when (keyword) {
            "applicantId" -> applicantId ?: ""
            "productType" -> applyInfo?.productType ?: ""
            "currentStep" -> applyInfo?.currentStep ?: ""
            "applyNumber" -> applyInfo?.applyNumber ?: ""
            "userName" -> applyInfo?.userName ?: ""
            "userPhoneNumber" -> applyInfo?.userPhoneNumber ?: ""
            "userResidentialNumber" -> applyInfo?.userResidentialNumber ?: ""
            else -> {
                Timber.w("❌ 알 수 없는 키워드: $keyword")
                ""
            }
        }
    }

    override fun clear() {
        applyInfo = null
    }
}