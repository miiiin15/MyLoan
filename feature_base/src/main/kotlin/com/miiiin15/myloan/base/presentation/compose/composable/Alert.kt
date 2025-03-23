package com.miiiin15.myloan.base.presentation.compose.composable

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import timber.log.Timber

private var showDialogState = mutableStateOf(false)

@Composable
fun ShowAlert(
    timestamp: Long = 0L,
    title: String = "알림",
    message: String,
    buttonText: String = "확인",
    onDismiss: () -> Unit = { }
) {
    showDialogState.value = true

    CustomAlert(
        timestamp,
        title,
        message,
        buttonText,
        onDismiss
    )
}

fun hideAlert() {
    showDialogState.value = false
}

@Composable
fun CustomAlert(
    timestamp: Long,
    title: String,
    message: String,
    buttonText: String,
    onDismiss: () -> Unit
) {
    if (showDialogState.value) {
        Timber.d("📣 ShowAlert($timestamp) : $message")
        AlertDialog(
            onDismissRequest = { onDismiss.invoke() },
            title = { TextDynamic(title) },
            text = { TextDynamic(message) },
            confirmButton = {
                Button(onClick = {
                    onDismiss.invoke()
                    hideAlert()
                }) {
                    TextDynamic(
                        text = buttonText,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        )
    }
}