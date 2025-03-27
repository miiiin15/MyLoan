package com.miiiin15.myloan.list.data.repository.fake

import com.miiiin15.myloan.list.domain.repository.fake.HardCodedContentRepository
import com.miiiin15.myloan.list.presentation.screen.apply.policy.AgreementItem
import kotlinx.collections.immutable.persistentListOf

class HardCodedContentRepositoryImpl : HardCodedContentRepository {
    override fun getAgreementItems(): List<AgreementItem> {
        return persistentListOf(
            AgreementItem("대출 한도 및 금리 안내", "대출 신청 시 적용 가능한 한도와 금리에 대한 상세 내용을 확인하고 동의하시겠습니까?"),
            AgreementItem(
                "연체 시 불이익 안내",
                "대출금 상환 지연 시 발생할 수 있는 연체 이자 및 신용도 하락 등의 불이익에 대해 숙지하고 동의하시겠습니까?"
            ),
            AgreementItem(
                "개인(신용)정보 제3자 제공 동의",
                "대출 심사 및 계약 이행을 위해 개인(신용)정보를 제3자에게 제공하는 것에 동의하시겠습니까?"
            ),
            AgreementItem(
                "대출 계약 철회권 안내",
                "대출 계약 후 일정 기간 내에 계약을 철회할 수 있는 권리가 있음을 안내받았으며, 이에 동의하시겠습니까?"
            )
        )
    }
}