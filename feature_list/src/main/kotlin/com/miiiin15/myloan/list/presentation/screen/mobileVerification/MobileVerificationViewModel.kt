package com.miiiin15.myloan.list.presentation.screen.mobileVerification

import androidx.lifecycle.viewModelScope
import com.miiiin15.myloan.base.common.util.SharedPreferenceManager
import com.miiiin15.myloan.base.presentation.nav.NavManager
import com.miiiin15.myloan.base.presentation.viewmodel.BaseAction
import com.miiiin15.myloan.base.presentation.viewmodel.BaseState
import com.miiiin15.myloan.base.presentation.viewmodel.BaseViewModel
import com.miiiin15.myloan.list.domain.model.mobileVerification.AuthConfirmation
import com.miiiin15.myloan.list.domain.model.mobileVerification.ContactInfo
import com.miiiin15.myloan.list.domain.model.mobileVerification.UserInfo
import com.miiiin15.myloan.list.domain.model.mobileVerification.VerificationInfo
import com.miiiin15.myloan.list.domain.model.mobileVerification.VerificationPurposeName
import com.miiiin15.myloan.list.domain.repository.ApplyInfoRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

internal class MobileVerificationViewModel(
    private val navManager: NavManager,
    private val applyInfoRepository: ApplyInfoRepository,
    private val sharedPreferenceManager: SharedPreferenceManager,
) : BaseViewModel<UiState, Action>(UiState.Initial) {

    private val _userInfo = MutableStateFlow(UserInfo())
    val userInfo: StateFlow<UserInfo> = _userInfo

    private val _contactInfo = MutableStateFlow(ContactInfo())
    val contactInfo: StateFlow<ContactInfo> = _contactInfo

    private val _verificationInfo = MutableStateFlow(VerificationInfo())
    val verificationInfo: StateFlow<VerificationInfo> = _verificationInfo

    private val _countdownTime = MutableStateFlow(0L) // 남은 시간 (초 단위)
    val countdownTime: StateFlow<Long> = _countdownTime

    private var countdownJob: Job? = null

    private fun startCountdown() {
        if (countdownJob?.isActive == true) {
            countdownJob?.cancel() // 이전 카운트 작업 취소
        }

        countdownJob = viewModelScope.launch {
            _countdownTime.value = 180L

            while (_countdownTime.value > 0) {
                delay(1000L)
                _countdownTime.value -= 1
            }
            sendAction(Action.VerificationExpired("인증번호가 만료되었습니다."))
        }
    }

    fun updateField(field: String, value: String) {
        _userInfo.value = when (field) {
            "name" -> _userInfo.value.copy(name = value)
            "birthDate" -> _userInfo.value.copy(birthDate = value)
            "residentialNumber" -> _userInfo.value.copy(residentialNumber = value)
            else -> _userInfo.value
        }

        _contactInfo.value = when (field) {
            "carrierCode" -> _contactInfo.value.copy(carrierCode = value)
            "phoneNumber" -> _contactInfo.value.copy(phoneNumber = value)
            else -> _contactInfo.value
        }

        _verificationInfo.value = when (field) {
            "verificationCode" -> _verificationInfo.value.copy(verificationCode = value)
            else -> _verificationInfo.value
        }
    }

    private fun validateVerificationState(): String? {
        return when {
            _userInfo.value.name.isNullOrEmpty() || _userInfo.value.name!!.length <= 1 -> "이름을 입력해주세요"
            _userInfo.value.birthDate.isNullOrEmpty() || _userInfo.value.birthDate!!.length < 6 -> "주민등록번호 앞 6자리를 입력해주세요"
            _userInfo.value.residentialNumber.isNullOrEmpty() || _userInfo.value.residentialNumber!!.length < 7 -> "주민등록번호 뒤 7자리를 입력해주세요"
            _contactInfo.value.carrierCode.isNullOrEmpty() -> "통신사를 선택해주세요"
            _contactInfo.value.phoneNumber.isNullOrEmpty() || _contactInfo.value.phoneNumber!!.length < 9 -> "휴대폰 번호를 입력해주세요"
            else -> null
        }
    }

    fun requestVerificationCode(mobileNumber: String) {
        val validate = validateVerificationState()
        if (validate != null) {
            sendAction(Action.Failure(validate))
            return
        }

        sendAction(Action.RequestCode(mobileNumber))
        viewModelScope.launch {
            try {
//               val transaction = requestVerificationCodeUseCase(mobileNumber)
//               sendAction(Action.CodeReceived(transaction, dateExpired))

                sendAction(Action.CodeReceived(180L))
                startCountdown()

            } catch (e: Exception) {
                sendAction(Action.VerificationFailure("인증번호 요청 실패: ${e.message}"))
            }
        }
    }

    fun confirmVerificationCode() {
        viewModelScope.launch {
            try {
//                val result = confirmTransactionUseCase(transaction, code)
                val fakeResult = if (_verificationInfo.value.verificationCode == "999999") {
                    AuthConfirmation(
                        transactionId = "123456",
                        phoneNumber = _contactInfo.value.phoneNumber ?: "",
                        token = System.currentTimeMillis().toString(),
                        verificationPurpose = VerificationPurposeName.LOAN_APPLY
                    )
                } else {
                    throw Exception("인증번호가 일치하지 않습니다.")
                }

                sharedPreferenceManager.putString("accessToken", fakeResult.token).let {
                    sendAction(Action.VerificationSuccess(fakeResult.token))
                }
            } catch (e: Exception) {
                sendAction(Action.VerificationFailure(e.message ?: "인증 실패"))
            } finally {
                updateField("verificationCode", "")
            }
        }
    }

    fun clear() {
        _userInfo.value = UserInfo()
        _contactInfo.value = ContactInfo()
        _verificationInfo.value = VerificationInfo()
        _countdownTime.value = 0L
        countdownJob?.cancel()
        sendAction(Action.Initialize(""))
    }


    fun onCompleteClick() {
        sharedPreferenceManager.getString("accessToken")?.let { _ ->
            applyInfoRepository.setApplyInfo(
                "applyNumber",
                UUID.randomUUID().toString().substring(0, 10)
            )
            applyInfoRepository.setApplyInfo("userName", _userInfo.value.name ?: "")
            applyInfoRepository.setApplyInfo(
                "userPhoneNumber",
                _contactInfo.value.phoneNumber ?: ""
            )
            applyInfoRepository.setApplyInfo(
                "userResidentialNumber",
                _userInfo.value.birthDate + _userInfo.value.residentialNumber
            )
            navManager.navigate(MobileVerificationFragmentDirections.actionMobileVerificationToLoanApplyPolicy())
        } ?: run {
            sendAction(Action.Failure("Access Token이 없습니다. 인증을 다시 시도해주세요."))
        }
    }
}


sealed interface Action : BaseAction<UiState> {
    data class Initialize(val message: String) : Action {
        override fun reduce(state: UiState): UiState {
            return UiState.Initial
        }
    }

    data class Failure(val errorMessage: String) : Action {
        override fun reduce(state: UiState): UiState {
            return UiState.Failure(System.currentTimeMillis(), errorMessage)
        }
    }

    data class RequestCode(val mobileNumber: String) : Action {
        override fun reduce(state: UiState): UiState {
            return UiState.CodeRequesting
        }
    }

    data class CodeReceived(val time: Long) : Action {
        override fun reduce(state: UiState): UiState {
            return UiState.CodeReceived
        }
    }

    data class VerificationSuccess(val accessToken: String) : Action {
        override fun reduce(state: UiState): UiState {
            return UiState.Completed(accessToken)
        }
    }

    data class VerificationExpired(val errorMessage: String) : Action {
        override fun reduce(state: UiState): UiState {
            return UiState.Failure(System.currentTimeMillis(), errorMessage)
        }
    }

    data class VerificationFailure(val errorMessage: String) : Action {
        override fun reduce(state: UiState): UiState {
            return UiState.CodeVerifyFailure(System.currentTimeMillis(), errorMessage)
        }
    }
}

sealed interface UiState : BaseState {
    object Initial : UiState
    object CodeRequesting : UiState
    object CodeReceived : UiState
    data class Completed(val accessToken: String) : UiState
    data class CodeVerifyFailure(val timestamp: Long, val errorMessage: String) : UiState
    data class Failure(val timestamp: Long, val errorMessage: String) : UiState
}

