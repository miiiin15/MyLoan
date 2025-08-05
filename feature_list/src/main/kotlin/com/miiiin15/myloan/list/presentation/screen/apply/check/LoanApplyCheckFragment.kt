package com.miiiin15.myloan.list.presentation.screen.apply.check

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.miiiin15.myloan.base.presentation.ext.collectInLaunchedEffectWithLifecycle
import kotlinx.coroutines.channels.Channel
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
    onCheckItem: (index: Int, checked: Boolean) -> Unit,
    onBackClick: () -> Unit,
    onSubmit: () -> Unit,
) {
    LoadingDialog(show = viewState.isLoading)
    BaseScreen(
        title = "대출 신청 전 확인",
        modifier = Modifier
            .padding(horizontal = Dimen.screenContentPadding),
        content = {},
        buttonEnabled = viewState.isValidate,
        buttonText = "다음",
        onBackClick = onBackClick,
        onButtonClick = onSubmit,
    )
}

