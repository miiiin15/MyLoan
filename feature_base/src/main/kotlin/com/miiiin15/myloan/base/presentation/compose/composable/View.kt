package com.miiiin15.myloan.base.presentation.compose.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.miiiin15.myloan.base.R
import com.miiiin15.myloan.base.common.res.Dimen

// 데이터가 없을 때 표시되는 애니메이션
@Composable
fun DataNotFoundAnim() {
    LabeledAnimation(R.string.data_not_found, R.raw.lottie_error_screen)
}

// 공사 중 화면을 표시하는 애니메이션
@Composable
fun UnderConstructionAnim() {
    LabeledAnimation(R.string.under_construction, R.raw.lottie_building_screen)
}

// 로딩 상태를 표시하는 프로그레스 인디케이터
@Composable
fun ProgressIndicator() {
    Box {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center) // 인디케이터를 중앙에 정렬
                .size(Dimen.spaceXXL),
        )
    }
}


@Composable
fun LoadingDialog(show: Boolean) {
    if (show) {
        Dialog(onDismissRequest = {}) {
            Surface(
                tonalElevation = 0.dp,
                color = MaterialTheme.colorScheme.background.copy(alpha = 0f),
                shape = MaterialTheme.shapes.medium
            ) {
                ProgressIndicator()
            }
        }
    }
}