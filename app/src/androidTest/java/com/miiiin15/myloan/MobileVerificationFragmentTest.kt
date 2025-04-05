package com.miiiin15.myloan

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.miiiin15.myloan.app.presentation.NavHostActivity
import org.junit.Rule
import org.junit.Test

class MobileVerificationFragmentTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<NavHostActivity>()

    @Test
    fun 휴대폰_인증_인증번호_오류(){
        // "휴대폰 인증" 화면이 나타날 때까지 최대 5초 대기
        composeTestRule.waitUntil(
            timeoutMillis = 30_000,
            condition = {
                composeTestRule.onAllNodesWithText("휴대폰 인증").fetchSemanticsNodes().isNotEmpty()
            }
        )
        composeTestRule.onNodeWithText("전체동의").performClick()

        composeTestRule.onNodeWithText("이름 입력").performTextInput("테스트")

        composeTestRule.onNodeWithText("주민번호 앞자리").performTextInput("999999")
        composeTestRule.onNodeWithText("주민번호 뒷자리").performTextInput("1111111")

        composeTestRule.onNodeWithText("통신사 선택").performClick()
        composeTestRule.onNodeWithText("LGU+").performClick()

        composeTestRule.onNodeWithText("휴대폰 번호 입력").performTextInput("01012345678")

        // 검증1. 휴대폰 번호 필드 글자수 채우면 인증 요청 버튼 활성화
        composeTestRule.onNodeWithTag("requestButton").assertIsEnabled()
        composeTestRule.onNodeWithTag("requestButton").performClick()

        // 검증2. 인증번호 입력 필드가 활성화되면 휴대폰 번호 필드가 비활성화
        composeTestRule.onNodeWithText("01012345678").assertIsNotEnabled()

        composeTestRule.onNodeWithText("인증번호 999999").performTextInput("888888")
        // 검증3. 인증번호 입력 필드에 글자수 채우면 인증 확인 버튼 활성화
        composeTestRule.onNodeWithTag("confirmButton").assertIsEnabled()
        composeTestRule.onNodeWithTag("confirmButton").performClick()

        // 검증4. 인증번호가 일치하지 않으면 오류 메시지 표시
        composeTestRule.onNodeWithText("인증번호가 일치하지 않습니다.").assertExists()

    }

    @Test
    fun 휴대폰_인증_인증_성공(){
        composeTestRule.waitUntil(
            timeoutMillis = 30_000,
            condition = {
                composeTestRule.onAllNodesWithText("휴대폰 인증").fetchSemanticsNodes().isNotEmpty()
            }
        )

        composeTestRule.onNodeWithText("전체동의").performClick()

        composeTestRule.onNodeWithText("이름 입력").performTextInput("테스트")

        composeTestRule.onNodeWithText("주민번호 앞자리").performTextInput("999999")
        composeTestRule.onNodeWithText("주민번호 뒷자리").performTextInput("1111111")

        composeTestRule.onNodeWithText("통신사 선택").performClick()
        composeTestRule.onNodeWithText("LGU+").performClick()

        composeTestRule.onNodeWithText("휴대폰 번호 입력").performTextInput("01012345678")

        composeTestRule.onNodeWithTag("requestButton").assertIsEnabled()
        composeTestRule.onNodeWithTag("requestButton").performClick()

        composeTestRule.onNodeWithText("01012345678").assertIsNotEnabled()

        composeTestRule.onNodeWithText("인증번호 999999").performTextInput("999999")
        composeTestRule.onNodeWithTag("confirmButton").assertIsEnabled()
        composeTestRule.onNodeWithTag("confirmButton").performClick()

        // 검증: 인증 번호가 일치하면 하단 버튼 활성화
        composeTestRule.onNodeWithTag("Base_Bottom_Button").assertIsEnabled()
        composeTestRule.onNodeWithTag("Base_Bottom_Button").performClick()

        // 검증: 다음 화면 타이틀이 보이는지 확인
        composeTestRule.onNodeWithText("대출 상품설명서").assertExists()
    }
}