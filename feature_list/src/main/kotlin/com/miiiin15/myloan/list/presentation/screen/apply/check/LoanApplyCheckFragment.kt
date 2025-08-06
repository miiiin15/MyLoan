package com.miiiin15.myloan.list.presentation.screen.apply.check

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miiii15.myloan.list.R
import com.miiiin15.myloan.base.common.res.Dimen
import com.miiiin15.myloan.base.presentation.activity.BaseFragment
import com.miiiin15.myloan.base.presentation.compose.composable.AlertManager
import com.miiiin15.myloan.base.presentation.compose.composable.BaseScreen
import com.miiiin15.myloan.base.presentation.compose.composable.GlobalAlertHost
import com.miiiin15.myloan.base.presentation.compose.composable.LoadingDialog
import com.miiiin15.myloan.base.presentation.compose.composable.TextDynamic
import com.miiiin15.myloan.base.presentation.compose.composable.TextType
import com.miiiin15.myloan.base.presentation.ext.collectInLaunchedEffectWithLifecycle
import com.miiiin15.myloan.list.presentation.component.CheckItemRow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import org.koin.androidx.navigation.koinNavGraphViewModel

class LoanApplyCheckFragment : BaseFragment() {

    private val viewModel: LoanApplyCheckViewModel by koinNavGraphViewModel(R.id.listNavGraph)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                LoanApplyCheckScreen(
                    viewModel = viewModel,
                )
            }
        }
    }

}

@Composable
fun LoanApplyCheckScreen(
    viewModel: LoanApplyCheckViewModel
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val intentChanel = remember { Channel<ViewIntent>(Channel.UNLIMITED) }
    val dispatch = remember {
        { intent: ViewIntent ->
            intentChanel.trySend(intent).getOrThrow()
        }
    }
    val scope = rememberCoroutineScope()

    BackHandler(enabled = true) {
        dispatch(ViewIntent.Back)
    }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.Main.immediate) {
            intentChanel
                .consumeAsFlow()
                .onStart { emit(ViewIntent.Initial) }
                .onEach(viewModel::processIntent)
                .collect()
        }
    }

    LaunchedEffect(viewModel.itemCheckedList) {
        dispatch(ViewIntent.Validate)
    }

    GlobalAlertHost()

    viewModel.singleEvent.collectInLaunchedEffectWithLifecycle { event ->
        when (event) {
            is SingleEvent.Failure -> {
                AlertManager.show(
                    title = "오류",
                    message = event.message,
                    buttonText = "닫기"
                )
            }

            is SingleEvent.BackAlert -> {
                AlertManager.show(
                    title = "알림",
                    message = event.message,
                    buttonText = "확인",
                    onClick = {}
                )
            }

            else -> {}
        }
    }

    LoanApplyCheckContent(
        viewState = viewState,
        checkItemList = viewModel.checkItems,
        onCheckItem = { index, checked ->
            dispatch(ViewIntent.ItemClicked(index, checked))
        },
        onBackClick = { dispatch(ViewIntent.Back) },
        onSubmit = { dispatch(ViewIntent.Submit) }
    )

}

@Composable
fun LoanApplyCheckContent(
    viewState: ViewState,
    checkItemList: List<CheckItem>,
    onCheckItem: (index: Int, checked: Boolean) -> Unit,
    onBackClick: () -> Unit,
    onSubmit: () -> Unit,
) {
    LoadingDialog(show = viewState.isLoading)
    BaseScreen(
        title = "대출 신청 전 확인",
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Dimen.screenContentPadding),
        content = {
            Spacer(modifier = Modifier.height(Dimen.spaceXL))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(Dimen.spaceM)
            ) {
                TextDynamic(
                    "당사의 대출상품은 고객님의 당사에만 대출을 진행하는 경우를 가정하여 고객님의 한도와 금리를 부여합니다.\n대출 이후 동시대출이 확인되는 경우 기한이익 상실 및 민형사상의 법 조치 등이 실행될 수 있어 현재 타사에 신청 및 심사진행중인 대출이 있는 경우, 타사의 대출 신청을 철회하시기를 당부드립니다.",
                    type = TextType.BodyMedium
                )
            }
            checkItemList.forEachIndexed { index, item ->
                key(index) {
                    CheckItemRow(
                        contents = item.content,
                        positiveText = "예",
                        negativeText = "아니오",
                        isChecked = viewState.checkedList.getOrNull(index) ?: false,
                        onCheckedChange = { checked -> onCheckItem(index, checked) }
                    )
                    Spacer(modifier = Modifier.padding(vertical = Dimen.spaceM))
                }
            }
        },
        buttonEnabled = viewState.isValidate,
        buttonText = if (viewState.isValidate) "확인" else "항목을 확인해 주세요",
        onBackClick = onBackClick,
        onButtonClick = onSubmit,
    )
}

