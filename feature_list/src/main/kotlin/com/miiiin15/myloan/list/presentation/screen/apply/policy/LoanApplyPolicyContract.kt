package com.miiiin15.myloan.list.presentation.screen.apply.policy

import androidx.compose.runtime.Immutable
import com.miiiin15.myloan.base.presentation.viewmodel2.MviIntent
import com.miiiin15.myloan.base.presentation.viewmodel2.MviSingleEvent
import com.miiiin15.myloan.base.presentation.viewmodel2.MviViewState
import com.miiiin15.myloan.list.domain.model.PolicyItem

data class AgreementItem(
    val title: String,
    val content: String,
)

// 화면 트리거
@Immutable
sealed interface ViewIntent : MviIntent {
    object Initial : ViewIntent
    data class PolicyChecked(val allPolicyChecked :Boolean) : ViewIntent
    data class AgreementCheck(val index: Int, val checked: Boolean) : ViewIntent
    object Validate : ViewIntent
}

@Immutable
data class ViewState(
    val policyList: List<PolicyItem>,
    val isAllPolicyChecked:Boolean,
    val agreementCheckedList: List<Boolean>,
    val isLoading: Boolean,
    val isValidate: Boolean,
    val isSubmitting: Boolean,
    val isSubmitted: Boolean,
    val errorMessage: String?,
) : MviViewState {
    companion object Factory {
        fun initial(): ViewState {
            return ViewState(
                policyList = emptyList(),
                isAllPolicyChecked = false,
                agreementCheckedList = listOf(false, false, false, false),
                isLoading = false,
                isValidate = false,
                isSubmitting = false,
                isSubmitted = false,
                errorMessage = null
            )
        }
    }
}

// 부분 상태 변화
sealed interface PartialStateChange {
    fun reduce(viewState: ViewState): ViewState

    sealed interface Policy : PartialStateChange {
        object Loading : Policy
        data class Data(val policy: List<PolicyItem>) : Policy
        data class Error(val errorMessage: String) : Policy

        override fun reduce(viewState: ViewState): ViewState =
            when (this) {
                Loading -> viewState.copy(
                    isLoading = true,
                )

                is Data -> viewState.copy(
                    policyList = policy,
                    isLoading = false,
                )

                is Error -> viewState.copy(
                    policyList = emptyList(),
                    isLoading = false,
                    errorMessage = errorMessage
                )
            }
    }

    data class PolicyChecked(val isAllPolicyChecked: Boolean) : PartialStateChange {
        override fun reduce(viewState: ViewState): ViewState =
            viewState.copy(isAllPolicyChecked = isAllPolicyChecked)
    }

    data class AgreementCheck(val checkedList: List<Boolean>) : PartialStateChange {
        override fun reduce(viewState: ViewState): ViewState =
            viewState.copy(agreementCheckedList = checkedList)
    }

    data class Validate(val isValidate: Boolean) : PartialStateChange {
        override fun reduce(viewState: ViewState): ViewState =
           viewState.copy(isValidate = isValidate)
    }
}

// 일회성 UI 이벤트 alert
sealed interface SingleEvent : MviSingleEvent {
    data class Failure(val errorMessage: String) : SingleEvent
}
