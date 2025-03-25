package com.miiiin15.myloan.base.presentation.compose.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.miiiin15.myloan.base.common.res.Dimen

@Composable
fun DoubleSwitch(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    onClick: (Boolean) -> Unit,
    positiveText: String,
    negativeText: String,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Button(
            onClick = { onClick(true) },
            colors = if (isChecked) ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            else ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier
                .weight(1f)
                .padding(end = Dimen.spaceS)
        ) {
            TextDynamic(
                text = positiveText,
                color = if (isChecked) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
        }
        Button(
            onClick = { onClick(false) },
            colors = if (!isChecked) ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            else ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier
                .weight(1f)
                .padding(start = Dimen.spaceS)
        ) {
            TextDynamic(
                text = negativeText,
                color = if (!isChecked) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}