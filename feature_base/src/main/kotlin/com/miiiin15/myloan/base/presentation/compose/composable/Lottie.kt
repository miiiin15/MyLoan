package com.miiiin15.myloan.base.presentation.compose.composable

import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.miiiin15.myloan.base.common.res.Dimen

@Composable
fun LabeledAnimation(@StringRes label: Int, @RawRes assetResId: Int) {
    // 카드 형태의 컨테이너를 생성
    Card(
        modifier = Modifier
            .wrapContentSize(),
    ) {
        // 수직으로 정렬된 콘텐츠를 포함하는 컬럼
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .wrapContentSize()
                .padding(Dimen.spaceXL),
        ) {
            // 텍스트를 표시 (라벨)
            TextDynamic(text = stringResource(label))
            // Lottie 애니메이션 로더 호출
            LottieAssetLoader(assetResId)
        }
    }
}

@Composable
fun LottieAssetLoader(@RawRes assetResId: Int) {
    // Lottie 애니메이션을 위한 Composition 객체를 기억
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(assetResId))

    // Lottie 애니메이션을 화면에 렌더링
    LottieAnimation(
        composition,
        modifier = Modifier.requiredSize(Dimen.imageSize)
        ,
    )
}
