package com.miiiin15.myloan.list.presentation.screen.apply.policy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miiii15.myloan.list.R
import com.miiiin15.myloan.base.common.res.Dimen
import com.miiiin15.myloan.base.presentation.activity.BaseFragment
import com.miiiin15.myloan.base.presentation.compose.composable.BaseScreen
import com.miiiin15.myloan.base.presentation.compose.composable.TextDynamic
import com.miiiin15.myloan.base.presentation.compose.composable.TextType
import com.miiiin15.myloan.list.presentation.component.PolicyList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import org.koin.androidx.navigation.koinNavGraphViewModel
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import com.miiiin15.myloan.base.presentation.compose.composable.DoubleSwitch
import com.miiiin15.myloan.base.presentation.compose.composable.ShowAlert
import com.miiiin15.myloan.base.presentation.ext.collectInLaunchedEffectWithLifecycle
import kotlinx.coroutines.launch

class LoanApplyPolicyFragment : BaseFragment() {

    private val viewModel: LoanApplyPolicyViewModel by koinNavGraphViewModel(R.id.listNavGraph)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                LoanApplyPolicyScreen(
                    viewModel = viewModel,
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        viewModel.clear()
    }
}

@Composable
fun LoanApplyPolicyScreen(
    viewModel: LoanApplyPolicyViewModel,
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    // ViewIntent를 무제한으로 담을 수 있는 Channel을 Compose recomposition 시에도 재사용하도록 remember로 생성
    val intentChannel = remember { Channel<ViewIntent>(Channel.UNLIMITED) }
    var showAlert by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }
    val dispatch = remember {
        { intent: ViewIntent ->
            intentChannel.trySend(intent).getOrThrow()
        }
    }
    val scope = rememberCoroutineScope()

    // 채널에서 인텐트 수집
    LaunchedEffect(Unit) {
        // Dispatchers.Main.immediate를 사용하여 즉시 메인 스레드에서 코루틴을 실행함
        withContext(Dispatchers.Main.immediate) {
            intentChannel
                .consumeAsFlow() // Channel에서 Flow로 변환하여 수집
                .onStart { emit(ViewIntent.Initial) } // 초기 Intent를 전송
                .onEach(viewModel::processIntent) // ViewModel에 Intent를 전달
                .collect() // Flow를 수집하여 처리

        }
    }

    // 유효성 검사
    LaunchedEffect(viewModel.agreementCheckedList, viewState.isAllPolicyChecked) {
        dispatch(ViewIntent.Validate)
    }


    // 단일 이벤트 수집
    viewModel.singleEvent.collectInLaunchedEffectWithLifecycle { event ->
        when (event) {
            is SingleEvent.Failure -> {
                scope.launch {
                    val errorMessage = event.errorMessage
                    alertMessage = errorMessage
                    showAlert = true
                }
            }
            else->{}
        }
    }

    // TODO: 구조 개선 필요
    if (showAlert) {
        ShowAlert(
            timestamp = System.currentTimeMillis(),
            title = "오류",
            message = alertMessage,
            buttonText = "닫기",
            onDismiss = { showAlert = false }
        )
    }

    // 화면 구성
    LoanApplyPolicyContent(
        viewState = viewState,
        onPolicyCheck = { list, checked -> dispatch(ViewIntent.PolicyChecked(list, checked)) },
        agreementList = viewModel.agreementItems,
        onAgreementCheck = { index, checked ->
            dispatch(ViewIntent.AgreementCheck(index, checked))
        },
        onSubmit = { dispatch(ViewIntent.Submit) }
    )
}

@Composable
fun LoanApplyPolicyContent(
    viewState: ViewState,
    onPolicyCheck: (List<String>, Boolean) -> Unit,
    agreementList: List<AgreementItem>,
    onAgreementCheck: (Int, Boolean) -> Unit,
    onSubmit: () -> Unit,
) {

    BaseScreen(
        title = "대출 신청 전 동의",
        modifier = Modifier
            .padding(horizontal = Dimen.screenContentPadding),
        content = {
            // 약관 항목
            PolicyList(
                policyList = viewState.policyList,
                modifier = Modifier.padding(top = Dimen.spaceXL, bottom = Dimen.spaceL),
                onAllPolicyAgreed = { list, checked -> onPolicyCheck(list, checked) },
            )
            // 동의 항목
            agreementList.forEachIndexed { index, item ->
                key(index) {
                    AgreeContent(
                        title = item.title,
                        contents = item.content,
                        isChecked = viewState.agreementCheckedList.getOrNull(index) ?: false,
                        onCheckedChange = { checked -> onAgreementCheck(index, checked) }
                    )
                    Spacer(modifier = Modifier.padding(vertical = Dimen.spaceM))
                }
            }
        },
        buttonEnabled = viewState.isValidate,
        buttonText = "다음",
        onButtonClick = onSubmit,
    )
}

@Composable
fun AgreeContent(
    title: String,
    contents: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val outlineColor = MaterialTheme.colorScheme.outline

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onPrimary)
            .drawBehind {
                // 아래쪽 선
                drawLine(
                    color = outlineColor,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = Dimen.spaceS.value
                )
            },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimen.screenContentPadding, vertical = Dimen.spaceM)
        ) {
            TextDynamic(title, type = TextType.BodyMedium, fontWeight = FontWeight.Bold)
            TextDynamic(
                contents,
                type = TextType.BodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
            DoubleSwitch(
                modifier = Modifier.padding(top = Dimen.spaceM),
                isChecked = isChecked,
                onClick = onCheckedChange,
                positiveText = "동의",
                negativeText = "미동의"
            )
        }
    }
}

