package com.miiiin15.myloan.list.presentation.screen.apply.check

import androidx.compose.runtime.Immutable
import com.miiiin15.myloan.base.presentation.viewmodel2.MviIntent
import com.miiiin15.myloan.base.presentation.viewmodel2.MviSingleEvent
import com.miiiin15.myloan.base.presentation.viewmodel2.MviViewState

@Immutable
data class CheckItem(
    val title: String,
    val content: String,
)

@Immutable
sealed interface ViewIntent : MviIntent {
    object Initial : ViewIntent
    object Back : ViewIntent
    data class ItemClicked(val index: Int, val checked: Boolean) : ViewIntent
    object Validate : ViewIntent
    object Submit : ViewIntent
}

@Immutable
data class ViewState(
    val checkedList: List<Boolean>,
    val isAllChecked: Boolean,
    val isLoading: Boolean,
    val isValidate: Boolean,
    val isSubmitting: Boolean,
    val isSubmitted: Boolean,
    val errorMessage: String?,
) : MviViewState{
    companion object Factory {
        fun initial(): ViewState {
            return ViewState(
                checkedList = listOf(true, true, true, true),
                isAllChecked = false,
                isLoading = false,
                isValidate = false,
                isSubmitting = false,
                isSubmitted = false,
                errorMessage = null
            )
        }
    }
}

sealed interface PartialStateChange {
    fun reduce(viewState: ViewState): ViewState

    data object Initial : PartialStateChange {
        override fun reduce(viewState: ViewState): ViewState =
            viewState.copy(isLoading = false)
    }

    data class BackClicked(val isBack: Boolean) : PartialStateChange {
        override fun reduce(viewState: ViewState): ViewState =
            viewState.copy(isLoading = false, isSubmitting = false, isSubmitted = false)
    }

    data class ItemChecked(val checkedList: List<Boolean>) : PartialStateChange {
        override fun reduce(viewState: ViewState): ViewState =
            viewState.copy(checkedList = checkedList)
    }

    data class Validate(val isValid: Boolean) : PartialStateChange {
        override fun reduce(viewState: ViewState): ViewState =
            viewState.copy(isValidate = isValid)
    }

    sealed interface Submit : PartialStateChange {
        object Submitting : Submit
        object Success : Submit
        data class Failure(val errorMessage: String) : Submit

        override fun reduce(viewState: ViewState): ViewState =
            when (this) {
                is Submitting -> viewState.copy(
                    isLoading = true,
                    isSubmitting = true,
                    isSubmitted = false,
                    errorMessage = null
                )

                is Success -> viewState.copy(
                    isLoading = false,
                    isSubmitting = false,
                    isSubmitted = true,
                    errorMessage = null
                )

                is Failure -> viewState.copy(
                    isLoading = false,
                    isSubmitting = false,
                    isSubmitted = false,
                    errorMessage = errorMessage
                )
            }
    }
}

sealed interface SingleEvent : MviSingleEvent{
    data class Failure(val message: String) : SingleEvent
    data class BackAlert(val message: String) : SingleEvent
    object SubmitSuccess : SingleEvent
}