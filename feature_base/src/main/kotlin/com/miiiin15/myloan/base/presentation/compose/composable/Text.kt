package com.miiiin15.myloan.base.presentation.compose.composable

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun TextDynamic(
    text: String,
    type: TextType = TextType.BodyMedium,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    fontWeight: FontWeight? = null,
) {
    val style = when (type) {
        TextType.TitleLarge -> MaterialTheme.typography.titleLarge
        TextType.TitleMedium -> MaterialTheme.typography.titleMedium
        TextType.BodyLarge -> MaterialTheme.typography.bodyLarge
        TextType.BodyMedium -> MaterialTheme.typography.bodyMedium
        TextType.BodySmall -> MaterialTheme.typography.bodySmall
    }.let { if (fontWeight != null) it.copy(fontWeight = fontWeight) else it }

    Text(
        text = text,
        modifier = modifier,
        style = style,
        color = color,
    )
}

enum class TextType {
    TitleLarge,
    TitleMedium,
    BodyLarge,
    BodyMedium,
    BodySmall
}

