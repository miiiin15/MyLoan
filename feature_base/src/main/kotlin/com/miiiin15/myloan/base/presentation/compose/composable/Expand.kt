package com.miiiin15.myloan.base.presentation.compose.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.miiiin15.myloan.base.R
import com.miiiin15.myloan.base.common.res.Dimen

@Composable
fun ExpandableSection(
    title: String,
    expanded: Boolean,
    onToggle: () -> Unit,
    contentModifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onToggle)
                .padding(horizontal = Dimen.screenContentPadding)
                .drawBehind {
                    drawLine(
                        color = Color.Black, // 테두리 색상
                        start = Offset(0f, size.height), // 밑줄 시작 위치
                        end = Offset(size.width, size.height), // 밑줄 끝 위치
                        strokeWidth = 1.dp.toPx() // 테두리 두께
                    )
                }
                .padding( top = Dimen.spaceL, bottom = Dimen.spaceM),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextDynamic(
                text = title, type = TextType.TitleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(
                    id = if (expanded) R.drawable.ic_arrow_up
                    else R.drawable.ic_arrow_down
                ),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
        if (expanded) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                   .then(contentModifier)
                    .padding(vertical = Dimen.spaceM) // 상하단 여백
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimen.screenContentPadding)
                ) {
                    content()
                }
            }
        }
    }
}