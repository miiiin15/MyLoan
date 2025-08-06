package com.miiiin15.myloan.list.presentation.screen.apply.check

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take


@OptIn(
    FlowPreview::class,
    ExperimentalCoroutinesApi::class
)
class LoanApplyCheckViewModel(
    private val navManager: NavManager,
    private val applyInfoRepository: ApplyInfoRepository,
    private val hardCodedContentRepository: HardCodedContentRepository,
) : AbstractMviViewModel<ViewIntent, ViewState, SingleEvent>() {
    override val viewState: StateFlow<ViewState>

    val checkItems = hardCodedContentRepository.getApplyCheckItems()
    var itemCheckedList by mutableStateOf(List(4) { true })

    init {
        applyInfoRepository.setApplyInfo("currentStep", "check")

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

        val initialFlow = filter { it is ViewIntent.Initial }
            .map { PartialStateChange.Initial }

        val backClickFlow = filter { it is ViewIntent.Back }
            .map { PartialStateChange.BackClicked(true) }

        val itemCheckFlow = filter { it is ViewIntent.ItemClicked }
            .map<ViewIntent,PartialStateChange> { intent ->
                val index = (intent as ViewIntent.ItemClicked).index
                val isChecked = intent.checked
                val newList = itemCheckedList.toMutableList().apply {
                    this[index] = isChecked
                }
                itemCheckedList = newList
                PartialStateChange.ItemChecked(newList)
            }

        val validateFlow = filter { it is ViewIntent.Validate }
            .map {
                val isValid = itemCheckedList.none { it }
                PartialStateChange.Validate(isValid)
            }

        val submitFlow = filter { it is ViewIntent.Submit }
            .flatMapConcat {
                flow {
                    emit(PartialStateChange.Submit.Submitting)
                    // TODO: 추가 작업 필요시 보충
                    emit(PartialStateChange.Submit.Success)
                }
            }


        return merge(
            initialFlow,
            backClickFlow,
            itemCheckFlow,
            validateFlow,
            submitFlow
        )
    }

    private fun Flow<PartialStateChange>.sendSingleEvent(): Flow<PartialStateChange> {
        return onEach { change ->
            val event = when (change) {
                is PartialStateChange.BackClicked -> SingleEvent.BackAlert("대출 신청이 진행중입니다. 홈화면으로 돌아가시겠습니까?")
                is PartialStateChange.Submit.Failure -> SingleEvent.Failure(change.errorMessage)
                is PartialStateChange.Submit.Success -> SingleEvent.SubmitSuccess

                is PartialStateChange.Initial -> return@onEach
                is PartialStateChange.ItemChecked -> return@onEach
                is PartialStateChange.Validate -> return@onEach
                is PartialStateChange.Submit.Submitting -> return@onEach

            }
            sendEvent(event)

            if(event is SingleEvent.SubmitSuccess) {
                // TODO: 정보 입력 화면 연결
                sendEvent(SingleEvent.Failure("준비중 입니다."))
            }
        }
    }
}