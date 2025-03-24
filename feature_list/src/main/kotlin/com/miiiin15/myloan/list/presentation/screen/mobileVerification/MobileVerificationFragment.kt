package com.miiiin15.myloan.list.presentation.screen.mobileVerification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miiii15.myloan.list.R
import com.miiiin15.myloan.base.common.res.Dimen
import com.miiiin15.myloan.base.presentation.activity.BaseFragment
import com.miiiin15.myloan.base.presentation.compose.composable.BaseScreen
import com.miiiin15.myloan.base.presentation.compose.composable.InputField
import com.miiiin15.myloan.base.presentation.compose.composable.InputType
import com.miiiin15.myloan.base.presentation.compose.composable.ProgressIndicator
import com.miiiin15.myloan.base.presentation.compose.composable.SelectField
import com.miiiin15.myloan.base.presentation.compose.composable.ShowAlert
import com.miiiin15.myloan.base.presentation.compose.composable.TextDynamic
import com.miiiin15.myloan.list.domain.model.PolicyItem
import com.miiiin15.myloan.list.domain.model.mobileVerification.ContactInfo
import com.miiiin15.myloan.list.domain.model.mobileVerification.UserInfo
import com.miiiin15.myloan.list.domain.model.mobileVerification.VerificationInfo
import com.miiiin15.myloan.list.presentation.component.PolicyList
import org.koin.androidx.navigation.koinNavGraphViewModel

class MobileVerificationFragment : BaseFragment() {

    private val viewModel: MobileVerificationViewModel by koinNavGraphViewModel(R.id.listNavGraph)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MobileVerificationScreen(viewModel)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clear()
    }
}

@Composable
private fun MobileVerificationScreen(viewModel: MobileVerificationViewModel) {
    val uiState: UiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()

    val personalInfo by viewModel.userInfo.collectAsStateWithLifecycle()
    val contactInfo by viewModel.contactInfo.collectAsStateWithLifecycle()
    val verificationInfo by viewModel.verificationInfo.collectAsStateWithLifecycle()

    val countdownTime = viewModel.countdownTime.collectAsStateWithLifecycle()
    val isCodeReceived =
        remember(uiState) { uiState is UiState.CodeReceived || uiState is UiState.CodeVerifyFailure }
    val isVerificationConfirmed = remember(uiState) { uiState is UiState.Completed }

    val fakePolicy = listOf(
        PolicyItem(
            id = "c1",
            title = "개인정보 수집 및 이용 동의",
            required = true,
            files = listOf("file1.pdf"),
            options = listOf("option1", "option2")
        ),
        PolicyItem(
            id = "b4",
            title = "서비스 이용 약관 동의",
            required = true,
            files = listOf("file2.pdf"),
            options = listOf("option3", "option4")
        ),
        PolicyItem(
            id = "a1",
            title = "마케팅 정보 수신 동의",
            required = false,
            files = listOf("file2.pdf"),
            options = listOf("option3", "option4")
        )
    )

    uiState.let {
        when (it) {
            is UiState.Failure, is UiState.CodeVerifyFailure -> {
                val errorMessage = when (it) {
                    is UiState.Failure -> it.errorMessage
                    is UiState.CodeVerifyFailure -> it.errorMessage
                    else -> "알 수 없는 오류가 발생했습니다."
                }
                val timestamp = when (it) {
                    is UiState.Failure -> it.timestamp
                    is UiState.CodeVerifyFailure -> it.timestamp
                    else -> 0L
                }
                ShowAlert(
                    timestamp = timestamp,
                    title = "오류",
                    message = errorMessage,
                    buttonText = "닫기",
                )
            }

            is UiState.CodeRequesting -> ProgressIndicator()
            else -> {}
        }
    }

    BaseScreen(
        title = "휴대폰 인증",
        modifier = Modifier
            .padding(horizontal = Dimen.screenContentPadding),
        buttonEnabled = isVerificationConfirmed,
        onButtonClick = viewModel::onCompleteClick,
        content = {
            // 약관 동의
            PolicyList(
                policyList = fakePolicy,
                modifier = Modifier.padding(top = Dimen.spaceXL, bottom = Dimen.spaceL),
            ) { }

            // 개인정보 입력
            MobileVerificationContent(
                isCodeReceived = isCodeReceived || isVerificationConfirmed,
                userInfo = personalInfo,
                updateField = viewModel::updateField,
            )

            // 통신사, 전화번호 입력
            MobileVerificationPhoneInput(
                isCodeReceived = isCodeReceived || isVerificationConfirmed,
                contactInfo = contactInfo,
                updateField = viewModel::updateField,
                requestVerificationCode = viewModel::requestVerificationCode
            )

            // 인증번호 입력
            if (isCodeReceived) {
                CountdownTimer(countdownTime.value)
                MobileVerificationCodeInput(
                    verificationInfo = verificationInfo,
                    updateField = viewModel::updateField,
                    onButtonClick = viewModel::confirmVerificationCode
                )
            }
        })
}

// 개인정보 입력
@Composable
private fun MobileVerificationContent(
    isCodeReceived: Boolean,
    userInfo: UserInfo,
    updateField: (String, String) -> Unit,
) {
    //이름
    InputField(
        value = userInfo.name ?: "",
        onValueChange = { updateField("name", it) },
        disable = isCodeReceived,
        fieldType = InputType.TEXT,
        placeholder = "이름 입력",
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Dimen.spaceL),
        maxLength = 5
    )
    Row(
        modifier = Modifier.padding(bottom = Dimen.spaceL)
    ) {
        // 주민번호 앞자리
        InputField(
            value = userInfo.birthDate ?: "",
            onValueChange = { updateField("birthDate", it) },
            disable = isCodeReceived,
            fieldType = InputType.NUMBER,
            placeholder = "주민번호 앞자리",
            modifier = Modifier
                .weight(1f),
            maxLength = 6,
            isMask = true

        )
        TextDynamic(" - ", modifier = Modifier.align(Alignment.CenterVertically))
        // 주민번호 뒷자리
        InputField(
            value = userInfo.residentialNumber ?: "",
            onValueChange = { updateField("residentialNumber", it) },
            disable = isCodeReceived,
            fieldType = InputType.RESIDENT_ID,
            placeholder = "주민번호 뒷자리",
            modifier = Modifier
                .weight(1f),
            maxLength = 7,
            isMask = true
        )
    }
}

// 통신사, 전화번호 입력
@Composable
fun MobileVerificationPhoneInput(
    isCodeReceived: Boolean,
    contactInfo: ContactInfo,
    updateField: (String, String) -> Unit,
    requestVerificationCode: (String) -> Unit

) {
    val buttonText = if (isCodeReceived) "재요청" else "인증번호"
    SelectField(
        value = contactInfo.carrierCode,
        options = listOf(
            "01" to "SKT",
            "02" to "KT",
            "03" to "LGU+"
        ),
        placeholder = "통신사 선택",
        onOptionSelected = { updateField("carrierCode", it) },
        disable = isCodeReceived,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Dimen.spaceL)
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = Dimen.spaceL)
    ) {
        InputField(
            value = contactInfo.phoneNumber ?: "",
            onValueChange = { updateField("phoneNumber", it) },
            disable = isCodeReceived,
            fieldType = InputType.PHONE,
            placeholder = "휴대폰 번호 입력",
            modifier = Modifier
                .weight(1f),
            maxLength = 11,
        )
        Button(
            enabled = contactInfo.phoneNumber?.length == 11,
            onClick = { requestVerificationCode(contactInfo.phoneNumber!!) },
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .height(Dimen.buttonHeight)
                .padding(start = Dimen.spaceM)
        ) {
            TextDynamic(
                text = buttonText,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

// 카운트다운 타이머
@Composable
fun CountdownTimer(countdownTime: Long) {
    if (countdownTime > 0) {
        TextDynamic(
            text = "남은 시간: ${countdownTime / 60}:${
                (countdownTime % 60).toString().padStart(2, '0')
            }",
        )
    }
}

// 인증번호 입력
@Composable
fun MobileVerificationCodeInput(
    verificationInfo: VerificationInfo,
    updateField: (String, String) -> Unit,
    onButtonClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = Dimen.spaceL)
    ) {
        InputField(
            value = verificationInfo.verificationCode ?: "",
            onValueChange = { updateField("verificationCode", it) },
            fieldType = InputType.NUMBER,
            placeholder = "인증번호 999999",
            modifier = Modifier.weight(1f),
            maxLength = 6,
        )
        Button(
            enabled = verificationInfo.verificationCode?.length == 6,
            onClick = onButtonClick,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .height(Dimen.buttonHeight)
                .padding(start = Dimen.spaceM)
        ) {
            TextDynamic(text = "확인", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

