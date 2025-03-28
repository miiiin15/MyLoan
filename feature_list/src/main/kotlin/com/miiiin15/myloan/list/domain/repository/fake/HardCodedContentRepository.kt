package com.miiiin15.myloan.list.domain.repository.fake

import com.miiiin15.myloan.list.presentation.screen.apply.policy.AgreementItem
import com.miiiin15.myloan.list.presentation.screen.apply.suitability.SuitabilityContent

interface HardCodedContentRepository {
    fun getAgreementItems(): List<AgreementItem>
    fun getSuitabilityItems(): List<SuitabilityContent>
}