package com.miiiin15.myloan.list.data.repository.fake

import com.miiiin15.myloan.list.domain.repository.fake.HardCodedContentRepository
import com.miiiin15.myloan.list.presentation.screen.apply.policy.AgreementItem
import com.miiiin15.myloan.list.presentation.screen.apply.suitability.SuitabilityContent
import com.miiiin15.myloan.list.presentation.screen.apply.suitability.SuitabilityListItem
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

    override fun getSuitabilityItems(): List<SuitabilityContent> {
        return persistentListOf(
            SuitabilityContent(
                type = "normal",
                title = "연령",
                suitabilityList = persistentListOf(
                    SuitabilityListItem("20대 이하", "01"),
                    SuitabilityListItem("30대", "02"),
                    SuitabilityListItem("40대", "03"),
                    SuitabilityListItem("50대", "04"),
                    SuitabilityListItem("60대 이상", "05"),
                )
            ),
            SuitabilityContent(
                type = "custom",
                title = "대출 목적",
                suitabilityList = persistentListOf(
                    SuitabilityListItem("주택 구입", "01"),
                    SuitabilityListItem("자동차 구입", "02"),
                    SuitabilityListItem("학자금", "03"),
                    SuitabilityListItem("개인 용도", "04"),
                    SuitabilityListItem("기타", "05"),
                )
            ),
            SuitabilityContent(
                type = "custom",
                title = "소득 수준",
                suitabilityList = persistentListOf(
                    SuitabilityListItem("600만원 이상 ~ 5,000만원 미만", "01"),
                    SuitabilityListItem("5,000만원 이상 ~ 1억원 미만", "02"),
                    SuitabilityListItem("1억원 이상", "03"),
                    SuitabilityListItem("소득 없음", "04")
                )
            ),
            SuitabilityContent(
                type = "normal",
                title = "부채",
                suitabilityList = persistentListOf(
                    SuitabilityListItem("없음", "01"),
                    SuitabilityListItem("1,000만원 이하", "02"),
                    SuitabilityListItem("1,000만원 초과 ~ 5,000만원 이하", "03"),
                    SuitabilityListItem("5,000만원 초과", "04")
                )
            ),
            SuitabilityContent(
                type = "normal",
                title = "신용 점수",
                suitabilityList = persistentListOf(
                    SuitabilityListItem("600점 이하", "01"),
                    SuitabilityListItem("600점 초과", "01"),
                    SuitabilityListItem("모름", "05")
                )
            ),
            SuitabilityContent(
                type = "normal",
                title = "고정 지출",
                suitabilityList = persistentListOf(
                    SuitabilityListItem("연간 소득을 초과", "01"),
                    SuitabilityListItem("연간 소득을 초과하지 않음", "02"),
                )
            )
        )
    }
}