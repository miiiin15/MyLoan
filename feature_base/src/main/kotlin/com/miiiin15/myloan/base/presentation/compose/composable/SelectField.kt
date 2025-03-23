package com.miiiin15.myloan.base.presentation.compose.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.miiiin15.myloan.base.common.res.Dimen

@Composable
fun SelectField(
    value: String?,
    disable: Boolean = false,
    options: List<Pair<String, String>>, // Key, Value 쌍의 리스트
    placeholder: String = "선택하세요",
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedValue = options.find { it.first == value }?.second ?: placeholder
    val backgroundColor = if (disable) {
        MaterialTheme.colorScheme.outline
    } else {
        MaterialTheme.colorScheme.onPrimary
    }
    val borderColor = if (expanded) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.outline
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(Dimen.inputHeight)
            .clip(MaterialTheme.shapes.small)
            .background(color = backgroundColor)
            .border(
                width = Dimen.spaceXS,
                color = borderColor,
                shape = MaterialTheme.shapes.small
            )
            .padding(Dimen.spaceM)
            .clickable { if (!disable) expanded = !expanded }
    ) {
        TextDynamic(
            text = selectedValue,
            color = if (value == null) MaterialTheme.colorScheme.surfaceDim else MaterialTheme.colorScheme.primary
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { (key, value) ->
                DropdownMenuItem(
                    onClick = {
                        onOptionSelected(key)
                        expanded = false
                    },
                    text = { TextDynamic(text = value) }
                )
            }
        }
    }
}