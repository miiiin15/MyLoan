package com.miiiin15.myloan.base.presentation.compose.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.miiiin15.myloan.base.common.res.Dimen
import com.miiiin15.myloan.base.common.util.AsteriskMaskTransformation

enum class InputType {
    TEXT,
    NUMBER,
    PHONE,
    RESIDENT_ID
}


@Composable
fun InputField(
    value: String,
    disable: Boolean = false,
    onValueChange: (String) -> Unit,
    fieldType: InputType = InputType.TEXT,
    modifier: Modifier = Modifier,
    maxLength: Int = Int.MAX_VALUE,
    isMask: Boolean = false, // 마스킹 여부
    placeholder: String = "",
) {
    var isFocused by remember { mutableStateOf(false) }
    val backgroundColor = if (disable) {
        MaterialTheme.colorScheme.outline
    } else {
        MaterialTheme.colorScheme.onPrimary
    }
    val borderColor = if(isFocused) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.outline
    }


    val inputType = remember(fieldType) {
        when (fieldType) {
            InputType.TEXT -> KeyboardOptions.Default
            InputType.NUMBER -> KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )

            InputType.PHONE -> KeyboardOptions.Default.copy(
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Phone
            )

            InputType.RESIDENT_ID -> KeyboardOptions.Default.copy(
                autoCorrectEnabled = false,

                keyboardType = if (isMask) KeyboardType.NumberPassword else KeyboardType.Number
            )
        }
    }

    val visualTransformation = remember(fieldType, isMask) {
        if (fieldType == InputType.RESIDENT_ID && isMask) {
            AsteriskMaskTransformation()
        } else {
            VisualTransformation.None
        }
    }

    BasicTextField(
        value = value,
        enabled = !disable,
        keyboardOptions = inputType,
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.primary
        ),
        onValueChange = {
            val digits = when (fieldType) {
                InputType.TEXT -> it.take(maxLength)
                InputType.PHONE, InputType.RESIDENT_ID, InputType.NUMBER -> it.filter { it.isDigit() }
                    .take(maxLength)
            }
            onValueChange(digits)
        },


        modifier = modifier
            .fillMaxWidth()
            .height(Dimen.inputHeight)
            .clip(MaterialTheme.shapes.small)
            .background(color =backgroundColor)
            .border(
                width = Dimen.spaceXS,
                color = borderColor,
                shape = MaterialTheme.shapes.small
            )
            .padding(Dimen.spaceM)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            },
        visualTransformation = visualTransformation,

        decorationBox = { innerTextField ->
            Box(contentAlignment = Alignment.CenterStart) {
                if (value.isEmpty()) {
                    TextDynamic(
                        text = placeholder,
                        color = MaterialTheme.colorScheme.surfaceDim
                    )
                }
                innerTextField()
            }
        }
    )
}

// 프리뷰
@Preview(showBackground = true, apiLevel = 34)
@Composable
private fun InputFieldPreview() {
    InputField(
        value = "",
        disable = true,
        onValueChange = {},
        fieldType = InputType.TEXT,
        modifier = Modifier.fillMaxWidth(),
        maxLength = 10,
        isMask = false,
        placeholder = "이름을 입력하세요"
    )
}

