package com.miiiin15.myloan.list.presentation.screen.apply.suitability

import androidx.compose.runtime.Immutable
import com.miiiin15.myloan.base.presentation.viewmodel2.MviIntent
import com.miiiin15.myloan.base.presentation.viewmodel2.MviSingleEvent
import com.miiiin15.myloan.base.presentation.viewmodel2.MviViewState

// 대출 적합성 정보
data class LoanSuitabilityInfo(
    val suitabilityList: List<Pair<String, String>>,
    val timestamp: Long
)

// 대출 적합성 화면 콘텐츠
data class SuitabilityContent(
    val type: String,
    val title: String,
    val suitabilityList: List<SuitabilityListItem>,
)

// 대출 적합성 리스트 아이템
data class SuitabilityListItem(
    val label: String,
    val value: String,
)

@Immutable
sealed interface ViewIntent : MviIntent {
    object Initial : ViewIntent
    object Back : ViewIntent
    data class SuitabilityCheck(val index: Int, val checked: Boolean) : ViewIntent
    object Validate : ViewIntent
    object Submit : ViewIntent
}

@Immutable
data class ViewState(
    val isLoading: Boolean = false,
    val suitabilityList: List<Pair<String, String>> = emptyList(),
    val isValidate: Boolean = false,
    val isSubmitting: Boolean = false,
    val isSubmitted: Boolean = false,
    val errorMessage: String? = null
) : MviViewState {
    companion object Factory {
        fun initial() = ViewState(
            isLoading = false,
            suitabilityList = emptyList(),
            isValidate = false,
            isSubmitting = false,
            isSubmitted = false,
            errorMessage = null
        )
    }
}

sealed interface PartialStateChange {
    fun reduce(viewState: ViewState): ViewState

    data class BackClicked(val isBack: Boolean) :
        PartialStateChange {
        override fun reduce(viewState: ViewState): ViewState =
            viewState.copy(isLoading = false, isSubmitting = false, isSubmitted = false)
    }

    sealed interface SuitabilitySetting : PartialStateChange {
        object Setting : SuitabilitySetting
        object Sucess : SuitabilitySetting

        override fun reduce(viewState: ViewState): ViewState {
            when (this) {
                is Setting -> return viewState.copy(isLoading = true)
                is Sucess -> return viewState.copy(isLoading = false)
            }
        }
    }

    data class Validate(val isValid: Boolean) : PartialStateChange {
        override fun reduce(viewState: ViewState): ViewState {
            return viewState.copy(isValidate = isValid)
        }
    }

}

sealed interface SingleEvent : MviSingleEvent {
    data class Failure(val errorMessage: String) : SingleEvent
    data class BackAlert(val message: String) :SingleEvent
}