package com.miiiin15.myloan.list.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.miiiin15.myloan.base.common.res.Dimen
import com.miiiin15.myloan.base.presentation.compose.composable.DoubleSwitch
import com.miiiin15.myloan.base.presentation.compose.composable.TextDynamic
import com.miiiin15.myloan.base.presentation.compose.composable.TextType


@Composable
fun CheckItemRow(
    title: String? = null,
    contents: String,
    positiveText: String = "동의",
    negativeText: String = "미동의",
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
            title?.let {
                TextDynamic(title, type = TextType.BodyMedium, fontWeight = FontWeight.Bold)
            }
            TextDynamic(
                contents,
                type = TextType.BodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
            DoubleSwitch(
                modifier = Modifier.padding(top = Dimen.spaceM),
                isChecked = isChecked,
                onClick = onCheckedChange,
                positiveText = positiveText,
                negativeText = negativeText
            )
        }
    }
}

@Preview(showBackground = true, apiLevel = 34)
@Composable
fun CheckItemRowPreview() {
    CheckItemRow(
        title = "대출 신청 전 확인",
        contents = "대출 신청을 위해 약관에 동의해 주세요.",
        isChecked = true,
        onCheckedChange = {}
    )
}