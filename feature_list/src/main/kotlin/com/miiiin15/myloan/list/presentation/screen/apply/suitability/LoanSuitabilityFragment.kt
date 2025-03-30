package com.miiiin15.myloan.list.presentation.screen.apply.suitability

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miiii15.myloan.list.R
import com.miiiin15.myloan.base.presentation.activity.BaseFragment
import com.miiiin15.myloan.base.presentation.compose.composable.BaseScreen
import com.miiiin15.myloan.base.presentation.compose.composable.ExpandableSection
import com.miiiin15.myloan.base.presentation.compose.composable.RadioButtonGroup
import com.miiiin15.myloan.base.presentation.compose.composable.ShowAlert
import com.miiiin15.myloan.base.presentation.ext.collectInLaunchedEffectWithLifecycle
import com.miiiin15.myloan.list.domain.model.ApplyInfo
import com.miiiin15.myloan.list.domain.model.ApplyUser
import com.miiiin15.myloan.list.presentation.screen.apply.component.SuitabilityContentHeader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.navigation.koinNavGraphViewModel

class LoanSuitabilityFragment : BaseFragment() {

    private val viewModel: LoanSuitabilityViewModel by koinNavGraphViewModel(R.id.listNavGraph)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                SuitabilityScreen(
                    viewModel = viewModel,
                )
            }
        }
    }
}

@Composable
fun SuitabilityScreen(
    viewModel: LoanSuitabilityViewModel
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val intentChannel = remember { Channel<ViewIntent>(Channel.UNLIMITED) }
    val dispatch = remember {
        { intent: ViewIntent ->
            intentChannel.trySend(intent)
        }
    }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        withContext(Dispatchers.Main.immediate) {
            intentChannel
                .consumeAsFlow()
                .onStart { emit(ViewIntent.Initial) }
                .onEach(viewModel::processIntent)
                .collect()

        }
    }

    var showAlert by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }

    viewModel.singleEvent.collectInLaunchedEffectWithLifecycle { event ->
        when (event) {
            is SingleEvent.Failure -> {
                scope.launch {
                    val errorMessage = event.errorMessage
                    alertMessage = errorMessage
                    showAlert = true
                }
            }
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


    SuitabilityContent(
        viewState = viewState,
        applyInfo = viewModel.applyInfo!!,
        suitabilityContent = viewModel.suitabilityItems,
        onSubmit = { dispatch(ViewIntent.Validate) }
    )
}

@Composable
fun SuitabilityContent(
    viewState: ViewState,
    applyInfo: ApplyUser,
    suitabilityContent: List<SuitabilityContent>,
    onSubmit: () -> Unit,
) {
    // 확장 상태를 리스트로 관리
    val expandedList = remember {
        mutableStateListOf<Boolean>().apply {
            repeat(suitabilityContent.size) { add(true) }
        }
    }

    BaseScreen(
        title = "대출 적합성",
        buttonText = "제출",
        onButtonClick = onSubmit,
        content = {
            SuitabilityContentHeader(
                name = applyInfo.userName,
                residentialNumber = applyInfo.userResidentialNumber,
                phoneNumber = applyInfo.userPhoneNumber
            )
            suitabilityContent.forEachIndexed { idx, content ->
                key(content.title) {
                    SuitabilityExpendedContent(
                        isExpended = expandedList[idx],
                        suitabilityContent = content,
                        onToggle = { expandedList[idx] = !expandedList[idx] },
                    )
                }
            }
        }
    )
}

@Composable
fun SuitabilityExpendedContent(
    isExpended: Boolean,
    suitabilityContent: SuitabilityContent,
    onToggle: () -> Unit,
) {
    var selectedIndex by remember { mutableStateOf(0) }
    ExpandableSection(
        title = suitabilityContent.title,
        expanded = isExpended,
        contentModifier = Modifier.background(MaterialTheme.colorScheme.background),
        onToggle = onToggle,
    ) {
        RadioButtonGroup(
            items = suitabilityContent.suitabilityList.map { it.label to it.value },
            selectedIndex = selectedIndex,
            onSelected = { selectedIndex = it }
        )
    }
}

//@Preview(apiLevel = 34)
//@Composable
//fun SuitabilityContentPreview() {
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(MaterialTheme.colorScheme.onPrimary)
//    ) {
//        SuitabilityExpendedContent(
//            true,
//            suitabilityContent = SuitabilityContent(
//                type = "custom",
//                title = "대출 적합성 확인",
//                suitabilityList = listOf(
//                    SuitabilityListItem("소득", "500만원"),
//                    SuitabilityListItem("신용등급", "1등급"),
//                    SuitabilityListItem("부채비율", "30%")
//                )
//            )
//        )
//    }
//}

