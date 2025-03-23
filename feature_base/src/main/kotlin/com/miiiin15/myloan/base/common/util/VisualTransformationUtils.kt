package com.miiiin15.myloan.base.common.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

// 주민등록번호 마스킹용
class ResidentIdMaskTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text.filter { it.isDigit() }.take(13)
        val masked = buildString {
            append(digits.take(6))
            if (digits.length > 6) append("-******")
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int =
                if (offset <= 6) offset else offset + 1

            override fun transformedToOriginal(offset: Int): Int =
                if (offset <= 6) offset else offset - 1
        }

        return TransformedText(AnnotatedString(masked), offsetMapping)
    }
}

// 비밀번호 마스킹용
class AsteriskMaskTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val maskedText = AnnotatedString("●".repeat(text.length))
        return TransformedText(maskedText, OffsetMapping.Identity)
    }
}

// 하이픈 포맷터
fun formatPhoneNumber(input: String): String {
    val digits = input.filter { it.isDigit() }
    return when {
        digits.length <= 3 -> digits
        digits.length <= 7 -> "${digits.substring(0, 3)}-${digits.substring(3)}"
        digits.length <= 11 -> "${digits.substring(0, 3)}-${digits.substring(3, 7)}-${digits.substring(7)}"
        else -> digits
    }
}

fun formatResidentId(input: String): String {
    val digits = input.filter { it.isDigit() }
    return when {
        digits.length <= 6 -> digits
        digits.length <= 13 -> "${digits.substring(0, 6)}-${digits.substring(6)}"
        else -> digits
    }
}

fun residentIdMasked(input: String): String {
    val digits = input.filter { it.isDigit() }
    return when {
        digits.length <= 6 -> digits
        digits.length <= 13 -> "${digits.substring(0, 6)}-******"
        else -> digits
    }
}