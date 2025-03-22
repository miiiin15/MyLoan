package com.miiiin15.myloan.list.presentation.screen.list.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import com.miiiin15.myloan.base.common.res.Dimen
import com.miiiin15.myloan.base.presentation.compose.composable.TextDynamic
import com.miiiin15.myloan.base.presentation.compose.composable.TextType
import com.miiiin15.myloan.list.domain.model.Product

@Composable
internal fun ProductCard(product: Product) {
    val backgroundColor =
        if (product.sale) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
    val nameColor =
        if (product.sale) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onErrorContainer
    val rateColor =
        if (product.sale) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.outline


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimen.spaceL)
                .zIndex(0f), // Row의 zIndex를 낮게 설정
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = Dimen.spaceM),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                TextDynamic(
                    text = product.name,
                    type = TextType.BodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = nameColor,
                )
                Spacer(modifier = Modifier.height(Dimen.spaceS))
                TextDynamic(
                    text = product.description,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Column(
                modifier = Modifier.wrapContentSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextDynamic(
                        text = "연",
                        type = TextType.BodyMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = Dimen.spaceS),
                        color = rateColor
                    )
                    TextDynamic(
                        text = product.rate,
                        type = TextType.BodyLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = Dimen.spaceXS),
                        color = rateColor
                    )
                    TextDynamic(
                        text = "%",
                        type = TextType.BodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = rateColor
                    )
                }
                Spacer(modifier = Modifier.height(Dimen.spaceS))
                TextDynamic(
                    text = product.limit,
                    type = TextType.BodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Preview(showBackground = true, apiLevel = 34)
@Composable
private fun ProductCardPreview() {
    ProductCard(
        product = Product(
            group = "LOAN",
            productType = "LOAN_CREDIT",
            name = "신용대출",
            description = "신용대출 상품 설명",
            rate = "7.4",
            limit = "최고 3천만원",
            sale = true
        )
    )
}