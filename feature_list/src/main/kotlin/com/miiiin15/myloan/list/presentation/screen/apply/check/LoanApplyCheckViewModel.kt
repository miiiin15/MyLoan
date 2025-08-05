package com.miiiin15.myloan.list.presentation.screen.apply.check

import androidx.lifecycle.viewModelScope
import com.miiiin15.myloan.base.presentation.nav.NavManager
import com.miiiin15.myloan.base.presentation.viewmodel2.AbstractMviViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take


@OptIn(
    FlowPreview::class,
    ExperimentalCoroutinesApi::class
)
class LoanApplyCheckViewModel(
    private val navManager: NavManager,
) : AbstractMviViewModel<ViewIntent, ViewState, SingleEvent>() {
    override val viewState: StateFlow<ViewState>

    init {
        val initialVS = ViewState.initial()

        viewState = merge(
            intentSharedFlow.filterIsInstance<ViewIntent.Initial>().take(1),
            intentSharedFlow.filterNot { it is ViewIntent.Initial }
        ).shareWhileSubscribed()
            .toPartialStateChangeFlow()
            .debugLog("🔵 부분 상태 변화")
            .sendSingleEvent()
            .scan(initialVS) { vs, change -> change.reduce(vs) }
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                initialVS
            )
    }

    private fun SharedFlow<ViewIntent>.toPartialStateChangeFlow(): Flow<PartialStateChange> {
        return merge()
    }

    private fun Flow<PartialStateChange>.sendSingleEvent(): Flow<PartialStateChange> {
        return onEach { change ->
            val event = when (change) {
                is PartialStateChange.BackClicked -> SingleEvent.BackAlert("대출 신청이 진행중입니다. 홈화면으로 돌아가시겠습니까?")
                is PartialStateChange.Submit.Failure -> SingleEvent.Failure(change.errorMessage)
                is PartialStateChange.Submit.Success -> SingleEvent.SubmitSuccess

                is PartialStateChange.ItemChecked -> return@onEach
                is PartialStateChange.Validate -> return@onEach
                is PartialStateChange.Submit.Submitting -> return@onEach

            }
            sendEvent(event)
        }
    }
}