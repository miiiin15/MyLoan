package com.miiiin15.myloan.result.presentation.screen.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.miiiin15.myloan.base.presentation.activity.BaseFragment

class ApplyResultFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return ComposeView(requireContext()).apply {
            setContent {
                ApplyResultScreen()
            }
        }
    }
}

@Composable
fun ApplyResultScreen() {
    val test = remember { mutableStateListOf<String>("Result 0") }
    LaunchedEffect(Unit) {
        test.addAll(listOf("Result 1", "Result 2", "Result 3"))
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        BasicText(text = "신청 결과")
        Spacer(modifier = Modifier.height(8.dp))
        test.forEach { result ->
            Text(
                text = result,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .shadow(10.dp, RoundedCornerShape(8.dp))
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ApplyResultScreenPreview() {
    ApplyResultScreen()
}