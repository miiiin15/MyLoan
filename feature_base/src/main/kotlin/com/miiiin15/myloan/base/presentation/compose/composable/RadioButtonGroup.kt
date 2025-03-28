package com.miiiin15.myloan.base.presentation.compose.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun RadioButtonGroup(
    items: List<Pair<String, String>>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
    lineSize: Int = 2
) {
    Row {
        items.chunked(lineSize).forEachIndexed { colIdx, columnItems ->
            Column(
                Modifier.weight(1f)
            ) {
                columnItems.forEachIndexed { rowIdx, pair ->
                    key(pair.second) { // value(고유값)로 key 할당
                        val index = items.indexOf(pair)
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable { onSelected(index) },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedIndex == index,
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = MaterialTheme.colorScheme.primary,
                                    unselectedColor = MaterialTheme.colorScheme.secondaryContainer
                                ),
                                onClick = { onSelected(index) }
                            )
                            TextDynamic(
                                type = TextType.BodyLarge,
                                text = pair.first,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(apiLevel = 34)
@Composable
fun RadioButtonGroupPreview() {
    var selectedIndex by remember { mutableStateOf(0) }
    Box(modifier = Modifier
        .fillMaxWidth()
        .background(androidx.compose.ui.graphics.Color.White)) {
        RadioButtonGroup(
            items = listOf(
                "옵션 1" to "값1", "옵션 2" to "값2", "옵션 3" to "값3", "옵션 4" to "값4",
                "옵션 5" to "값5"
            ),
            selectedIndex = selectedIndex,
            onSelected = { selectedIndex = it },
            lineSize = 2
        )
    }
}