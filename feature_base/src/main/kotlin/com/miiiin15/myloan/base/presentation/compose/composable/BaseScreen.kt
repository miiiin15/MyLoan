package com.miiiin15.myloan.base.presentation.compose.composable

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.miiiin15.myloan.base.R
import com.miiiin15.myloan.base.common.res.Dimen
import kotlin.math.absoluteValue

@Composable
fun BaseScreen(
    title: String? = null,
    headerVisible: Boolean = true,
    onBackClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.onPrimary,
    buttonText: String = "확인",
    buttonEnabled: Boolean = true,
    onButtonClick: (() -> Unit)? = null,
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val headerVisibility = remember { mutableStateOf(headerVisible) }
    val previousScrollOffset = remember { mutableStateOf(0) }
    val scrollState = rememberScrollState()


    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.value }
            .collect { currentScrollOffset ->
                if ((currentScrollOffset - previousScrollOffset.value).absoluteValue > 2) { // 임계값 5 설정
                    if (currentScrollOffset > previousScrollOffset.value) {
                        headerVisibility.value = false // 아래로 스크롤
                    } else if (currentScrollOffset < previousScrollOffset.value) {
                        headerVisibility.value = true // 위로 스크롤
                    }
                }
                previousScrollOffset.value = currentScrollOffset // 이전 스크롤 위치 업데이트
            }
    }


    Box(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures { offset ->
                focusManager.clearFocus()
                val imm =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(
                    (context as? android.app.Activity)?.window?.decorView?.windowToken,
                    0
                )
            }
        }
    ) {

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // 상단 헤더
            HeaderBar(
                title = title ?: "",
                onBackClick = onBackClick
            )

            // 컨텐츠 영역
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(top = Dimen.spaceXL)
                    .background(backgroundColor)
            ) {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .background(backgroundColor)
                        .padding(bottom = Dimen.spaceXXL),
                    content = content
                )
            }
        }
        // 하단 버튼
        if (scrollState.value == scrollState.maxValue || scrollState.maxValue == 0) {
            onButtonClick.let {
                BottomButton(
                    text = buttonText,
                    enable = buttonEnabled,
                    onClick = onButtonClick ?: {},
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }

    }
}

@Composable
private fun HeaderBar(
    title: String,
    onBackClick: (() -> Unit)? = null
) {
    val horizontalArrangement =
        if (onBackClick == null) Arrangement.Center else Arrangement.SpaceBetween
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimen.spaceXL)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = Dimen.screenContentPadding, vertical = Dimen.spaceXS),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = horizontalArrangement
    ) {
        onBackClick?.let {
            IconButton(
                onClick = it,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_left),
                    contentDescription = "뒤로가기"
                )
            }
        }
        TextDynamic(
            text = title,
            type = TextType.TitleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        onBackClick?.let {
            Spacer(modifier = Modifier.width(40.dp)) // 뒤로가기 버튼이 있을 때만 공간 확보
        }
    }
}

@Preview(showBackground = true, apiLevel = 34)
@Composable
fun headerBarPreview() {
    HeaderBar(
        title = "헤더 타이틀",
        onBackClick = {}
    )
}

@Composable
private fun BottomButton(
    text: String,
    enable: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimen.spaceL)
    ) {
        Button(
            onClick = onClick,
            enabled = enable,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextDynamic(text = text, color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}