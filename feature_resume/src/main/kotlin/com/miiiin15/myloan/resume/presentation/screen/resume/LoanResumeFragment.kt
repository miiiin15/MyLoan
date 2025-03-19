package com.miiiin15.myloan.resume.presentation.screen.resume

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
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.miiiin15.myloan.base.presentation.activity.BaseFragment

class LoanResumeFragment: BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                LoanResumeStatefulScreen()
            }
        }
    }
}

@Composable
fun LoanResumeStatefulScreen(){
    val test = remember { mutableStateListOf<String>("Loan 0") }
    LoanResumeScreen(
        results = test,
        onAddResults = {
            test.addAll(listOf("Loan 1", "Loan 2", "Loan 3"))
        }
    )
}

@Composable
fun LoanResumeScreen(
    results: List<String>, // 상태를 매개변수로 전달
    onAddResults: () -> Unit // 상태 변경을 위한 콜백 전달
) {
    LaunchedEffect(Unit) {
        onAddResults() // 상태 변경 콜백 호출
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        BasicText(text = "이어하기")
        Spacer(modifier = Modifier.height(8.dp))
        results.forEach { result ->
            Text(
                text = result,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .background(Color.Red)
                    .padding(8.dp)
            )
        }
    }
}


