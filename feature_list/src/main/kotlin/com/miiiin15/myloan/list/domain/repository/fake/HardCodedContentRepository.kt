package com.miiiin15.myloan.list.domain.repository.fake

import com.miiiin15.myloan.list.presentation.screen.apply.policy.AgreementItem

interface HardCodedContentRepository {
    fun getAgreementItems(): List<AgreementItem>
}