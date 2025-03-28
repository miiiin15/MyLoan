package com.miiiin15.myloan.list.presentation.screen.apply.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.miiiin15.myloan.base.common.util.formatPhoneNumber
import com.miiiin15.myloan.base.common.util.residentIdMasked
import com.miiiin15.myloan.base.presentation.compose.composable.TextDynamic
import com.miiiin15.myloan.base.presentation.compose.composable.TextType
import kotlinx.collections.immutable.persistentListOf

@Composable
fun SuitabilityContentHeader(
    name: String,
    residentialNumber: String,
    phoneNumber: String
) {
    val outlineColor = MaterialTheme.colorScheme.outline
    val profileInfo = persistentListOf(
        Pair("성명", name),
        Pair("주민등록번호", residentIdMasked(residentialNumber)),
        Pair("연락처", formatPhoneNumber(phoneNumber))
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()

            .background(MaterialTheme.colorScheme.onPrimary).padding(
                top = Dimen.spaceXL,
                start = Dimen.screenContentPadding,
                end = Dimen.screenContentPadding,
            )
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
        ) {
            Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                TextDynamic(
                    modifier = Modifier.padding(
                        horizontal = Dimen.screenContentPadding,
                        vertical = Dimen.spaceL
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    text = "※ 본 확인서는 「금융소비자 보호에 대한 법률」 제17조(적합성원칙) 및 제18조(적정성 원칙)을 준수하니 유념하여 주시기 바랍니다."
                )
            }
            Column(
                modifier = Modifier.padding(
                    horizontal = Dimen.screenContentPadding,
                    vertical = Dimen.spaceM
                )
            ) {
                profileInfo.forEachIndexed({ index, pair ->
                    Row {
                        TextDynamic(
                            pair.first,
                            type = TextType.TitleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.weight(1f)
                        )
                        TextDynamic(
                            pair.second,
                            type = TextType.TitleMedium,
                            modifier = Modifier.weight(1f)
                        )
                    }
                })
            }
        }
    }
    Spacer(modifier = Modifier.padding(vertical = Dimen.spaceM))
}

@Preview(apiLevel = 34)
@Composable
fun SuitabilityContentPreview() {
    SuitabilityContentHeader(
        name = "홍길동",
        residentialNumber = "1234561234567",
        phoneNumber = "01012345678"
    )
}