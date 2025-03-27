package com.miiiin15.myloan.list.presentation.screen.apply.policy

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.miiiin15.myloan.base.common.util.SharedPreferenceManager
import com.miiiin15.myloan.base.presentation.nav.NavManager
import com.miiiin15.myloan.base.presentation.viewmodel2.AbstractMviViewModel
import com.miiiin15.myloan.list.domain.model.PolicyItem
import com.miiiin15.myloan.list.domain.model.apply.LoanApplyState
import com.miiiin15.myloan.list.domain.usecase.SubmitLoanAgreementUseCase
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import com.miiiin15.myloan.base.domain.result.Result
import com.miiiin15.myloan.list.domain.repository.ApplyInfoRepository
import com.miiiin15.myloan.list.domain.repository.fake.HardCodedContentRepository
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow

@OptIn(
    FlowPreview::class,
    ExperimentalCoroutinesApi::class,
)
class LoanApplyPolicyViewModel(
    private val navManager: NavManager,
    private val applyInfoRepository: ApplyInfoRepository,
    private val hardCodedContentRepository: HardCodedContentRepository,
    private val sharedPreferenceManager: SharedPreferenceManager,
    private val submitLoanAgreementUseCase: SubmitLoanAgreementUseCase,
) : AbstractMviViewModel<ViewIntent, ViewState, SingleEvent>() {
    override val viewState: StateFlow<ViewState>

    private var accessToken: String? = null
    val agreementItems = hardCodedContentRepository.getAgreementItems()
    var agreementCheckedList by mutableStateOf(List(4) { false })

    init {
        accessToken = sharedPreferenceManager.getString("accessToken")
        applyInfoRepository.setApplyInfo("currentStep", "agreement")

        val initialVS = ViewState.initial()

        // 인텐트 → PartialStateChange → ViewState 이렇게 상태가 점진적으로 업데이트되어 UI에 반영됩니다.
        viewState = merge(
            intentSharedFlow.filterIsInstance<ViewIntent.Initial>().take(1), // 초기 인텐트는 한 번만 처리
            intentSharedFlow.filterNot { it is ViewIntent.Initial } // 초기 인텐트를 제외한 나머지 인텐트는 계속 처리
        ).shareWhileSubscribed() // 구독자가 있을 때만 Flow를 유지, 스트림을 공유
            .toPartialStateChangeFlow()
            .debugLog("🔵 부분 상태 변화")
            .sendSingleEvent()
            .scan(initialVS) { vs, change -> change.reduce(vs) } // 이전 상태(vs)와 변화(change)를 받아서 새로운 상태로 누적합니다.
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                initialVS
            ) // 위에서 만든 Flow를 StateFlow로 변환해서, 항상 최신 상태를 보관하고 UI에서 관찰할 수 있게 합니다.

    }

    private fun Flow<PartialStateChange>.sendSingleEvent(): Flow<PartialStateChange> {
        return onEach { change ->
            val event = when (change) {
                is PartialStateChange.Policy.Error -> SingleEvent.Failure(change.errorMessage)
                is PartialStateChange.Submit.Failure -> SingleEvent.Failure(change.errorMessage)
                is PartialStateChange.Submit.Success -> SingleEvent.SubmitSuccess

                PartialStateChange.Policy.Loading -> return@onEach
                is PartialStateChange.Policy.Data -> return@onEach
                is PartialStateChange.PolicyChecked -> return@onEach
                is PartialStateChange.Validate -> return@onEach
                is PartialStateChange.AgreementCheck -> return@onEach
                PartialStateChange.Submit.Submitting -> return@onEach
            }
            sendEvent(event)

            if (event is SingleEvent.SubmitSuccess) {
                // TODO: 화면 이동
            }
        }
    }

    private fun SharedFlow<ViewIntent>.toPartialStateChangeFlow(): Flow<PartialStateChange> {
        // TODO: 약관 조회 api 연결
        val fakePolicy = persistentListOf(
            PolicyItem(
                id = "c1",
                title = "대출 상품설명서",
                required = true,
                files = listOf("file1.pdf"),
                options = listOf("option1", "option2")
            ),
            PolicyItem(
                id = "c2",
                title = "개인정보 수집 및 이용 동의서",
                required = true,
                files = listOf("file2.pdf"),
                options = listOf("option3", "option4")
            ),
            PolicyItem(
                id = "c3",
                title = "신용정보 수집 및 이용 동의서",
                required = true,
                files = listOf("file3.pdf"),
                options = listOf("option5", "option6")
            ),
            PolicyItem(
                id = "c4",
                title = "전자금융거래 이용약관",
                required = true,
                files = listOf("file4.pdf"),
                options = listOf("option7", "option8")
            ),
        )

        val initialFlow = filter { it is ViewIntent.Initial }
            .map<ViewIntent, PartialStateChange> { PartialStateChange.Policy.Data(fakePolicy) }
            .onStart { emit(PartialStateChange.Policy.Loading) }

        val policyCheckFlow = filter { it is ViewIntent.PolicyChecked }
            .map<ViewIntent, PartialStateChange> { intent ->
                val isAllChecked = (intent as ViewIntent.PolicyChecked).allPolicyChecked
                val checkedList = (intent as ViewIntent.PolicyChecked).checkedList
                PartialStateChange.PolicyChecked(checkedList, isAllChecked)
            }

        val agreementCheckFlow = filter { it is ViewIntent.AgreementCheck }
            .map<ViewIntent, PartialStateChange> { intent ->
                val index = (intent as ViewIntent.AgreementCheck).index
                val isChecked = intent.checked
                val newList = agreementCheckedList.toMutableList().apply {
                    this[index] = isChecked
                }
                agreementCheckedList = newList
                PartialStateChange.AgreementCheck(newList)
            }

        val validateFlow = filter { it is ViewIntent.Validate }
            .map<ViewIntent, PartialStateChange> {
                PartialStateChange.Validate(viewState.value.isAllPolicyChecked && agreementCheckedList.all { it })
            }

        val submitFlow = filter { it is ViewIntent.Submit }
            .flatMapConcat {
                flow {
                    val applicantId = applyInfoRepository.getApplyInfo("applicantId")
                    val applyInfo = applyInfoRepository.getApplyAllInfo()
                    val applySate = LoanApplyAgreementInfo(
                        agreedPolicyList = viewState.value.checkedPolicyList,
                        timestamp = System.currentTimeMillis()
                    )

                    emit(PartialStateChange.Submit.Submitting)
                    val result = submitLoanAgreementUseCase(
                        LoanApplyState(
                            applicantId = applicantId,
                            productType = applyInfo?.productType ?: "",
                            applyNumber = applyInfo?.applyNumber ?: "",
                            applyStep = applyInfo?.currentStep ?: "",
                            accessToken = accessToken!!,
                            timeStamp = System.currentTimeMillis(),
                            applyState = applySate
                        )
                    )
                    when (result) {
                        is Result.Success -> emit(PartialStateChange.Submit.Success)
                        is Result.Failure -> emit(
                            PartialStateChange.Submit.Failure(
                                result.throwable?.message ?: "Unknown error"
                            )
                        )
                    }
                }
            }

        return merge(
            initialFlow,
            policyCheckFlow,
            agreementCheckFlow,
            validateFlow,
            submitFlow
        )
    }
}
