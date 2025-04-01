package com.miiiin15.myloan.list.presentation.screen.apply.suitability

import androidx.lifecycle.viewModelScope
import com.miiiin15.myloan.base.presentation.nav.NavManager
import com.miiiin15.myloan.base.presentation.viewmodel2.AbstractMviViewModel
import com.miiiin15.myloan.list.domain.repository.ApplyInfoRepository
import com.miiiin15.myloan.list.domain.repository.fake.HardCodedContentRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take

@OptIn(
    FlowPreview::class,
    ExperimentalCoroutinesApi::class,
)
class LoanSuitabilityViewModel(
    private val navManager: NavManager,
    private val applyInfoRepository: ApplyInfoRepository,
    private val hardCodedContentRepository: HardCodedContentRepository,
) : AbstractMviViewModel<ViewIntent, ViewState, SingleEvent>() {
    override val viewState: StateFlow<ViewState>
    val applyInfo = applyInfoRepository.getApplyUser()
    val suitabilityItems = hardCodedContentRepository.getSuitabilityItems()

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

    fun backClicked(){
        navManager.popBackStack()
    }

    private fun SharedFlow<ViewIntent>.toPartialStateChangeFlow(): Flow<PartialStateChange> {

        val initialFlow = filter { it is ViewIntent.Initial }
            .onStart { emit(ViewIntent.Initial) }
            .map<ViewIntent, PartialStateChange> { PartialStateChange.SuitabilitySetting.Setting }

        val backClickFlow = filter { it is ViewIntent.Back }
            .map { PartialStateChange.BackClicked(true) }

        // TODO: 개발 진행중
        val validateFlow = filter { it is ViewIntent.Validate }
            .flatMapConcat {
                flow {
                    emit(PartialStateChange.Validate(false))
                }
            }

        return merge(
            initialFlow, backClickFlow, validateFlow
        )
    }

    private fun Flow<PartialStateChange>.sendSingleEvent(): Flow<PartialStateChange> {
        return onEach { change ->
            val event = when (change) {
                // TODO: 개발 진행중
                is PartialStateChange.Validate -> SingleEvent.Failure("개발 진행중 입니다.")
                is PartialStateChange.BackClicked -> SingleEvent.BackAlert("대출 신청이 진행중입니다. 홈화면으로 돌아가시겠습니까?")

                is PartialStateChange.SuitabilitySetting.Setting -> return@onEach
                is PartialStateChange.SuitabilitySetting.Sucess -> return@onEach
            }
            sendEvent(event)
        }
    }
}

