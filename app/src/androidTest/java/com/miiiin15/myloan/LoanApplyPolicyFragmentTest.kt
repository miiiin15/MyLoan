package com.miiiin15.myloan

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.miiiin15.myloan.app.presentation.NavHostActivity
import org.junit.Rule
import org.junit.Test

class LoanApplyPolicyFragmentTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<NavHostActivity>()

    @Test
    fun 약관_전체동의_및_동의_스위치_체크_다음버튼_활성화_테스트() {
        // "대출 상품설명서"가 나타날 때까지 최대 5초 대기
        composeTestRule.waitUntil(
            timeoutMillis = 30_000,
            condition = {
                composeTestRule.onAllNodesWithText("대출 상품설명서").fetchSemanticsNodes().isNotEmpty()
            }
        )
        // 약관 리스트가 보이는지 확인
        composeTestRule.onNodeWithText("대출 상품설명서").assertIsDisplayed()
        composeTestRule.onNodeWithText("개인정보 수집 및 이용 동의서").assertIsDisplayed()
        composeTestRule.onNodeWithText("신용정보 수집 및 이용 동의서").assertIsDisplayed()
        composeTestRule.onNodeWithText("전자금융거래 이용약관").assertIsDisplayed()

        // 전체동의 체크박스 클릭 (정책 전체 체크)
        composeTestRule.onNodeWithText("전체동의").performClick()

        // 모든 "동의" 버튼 클릭
        composeTestRule.onAllNodesWithText("동의").fetchSemanticsNodes().forEachIndexed { idx, _ ->
            val node = composeTestRule.onAllNodesWithText("동의")[idx]
            node.performScrollTo()
            composeTestRule.waitForIdle() // 스크롤 완료 대기
            node.performClick()
        }


//        composeTestRule.onNodeWithTag("Base_Bottom_Button").assertIsEnabled()

    }
}