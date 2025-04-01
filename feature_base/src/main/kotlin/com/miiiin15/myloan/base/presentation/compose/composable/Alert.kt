package com.miiiin15.myloan.base.presentation.compose.composable

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object AlertManager {
    private val _alertFlow = MutableSharedFlow<AlertData>(extraBufferCapacity = 1)
    val alertFlow = _alertFlow.asSharedFlow()

    fun show(
        title: String = "알림",
        message: String,
        buttonText: String = "확인",
        onDismiss: (() -> Unit)? = null,
        onClick: (() -> Unit)? = null
    ) {
        _alertFlow.tryEmit(AlertData(title, message, buttonText, onDismiss, onClick))
    }
}

data class AlertData(
    val title: String,
    val message: String,
    val buttonText: String,
    val onDismiss: (() -> Unit)?,
    val onClick: (() -> Unit)?
)

@Composable
fun GlobalAlertHost() {
    var currentAlert by remember { mutableStateOf<AlertData?>(null) }

    LaunchedEffect(Unit) {
        AlertManager.alertFlow.collect { alert ->
            currentAlert = alert
        }
    }

    currentAlert?.let { alert ->
        AlertDialog(
            onDismissRequest = {
                currentAlert = null
                alert.onDismiss?.invoke()
            },
            title = { TextDynamic(alert.title) },
            text = { TextDynamic(alert.message) },
            confirmButton = {
                Button(onClick = {
                    currentAlert = null
                    alert.onClick?.invoke()
                }) {
                    TextDynamic(
                        text = alert.buttonText,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        )
    }
}