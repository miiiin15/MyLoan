package com.miiiin15.myloan.list.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import com.miiiin15.myloan.base.common.res.Dimen
import com.miiiin15.myloan.base.presentation.compose.composable.TextDynamic
import com.miiiin15.myloan.list.domain.model.PolicyItem


@Composable
fun PolicyList(
    policyList: List<PolicyItem>,
    modifier: Modifier = Modifier,
    allPolicyAgreed: (Boolean) -> Unit = {}, // TODO: 동의 code 리스트 까지 같이 보낼 것
    onClick: () -> Unit ={}
) {
    val outlineColor = MaterialTheme.colorScheme.outline

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onPrimary)
            .drawBehind {
                // 아래쪽 선
                drawLine(
                    color = outlineColor,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = Dimen.spaceS.value
                )
                // 위쪽 선
                drawLine(
                    color = outlineColor,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = Dimen.spaceS.value
                )
            }
            .clickable { onClick() },
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(Dimen.spaceM)
        ) {
            policyList.forEachIndexed { index, policy ->
                val requiredText = if (policy.required) "[필수] " else "[선택] "
                val requireTextColor = if (policy.required) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.primary
                }
               Row(
                          verticalAlignment = Alignment.CenterVertically,
                          modifier = Modifier.fillMaxWidth().height(Dimen.spaceXL)
                      ) {
                          Checkbox(
                              checked = false, // TODO:  실제 체크박스 상태를 연결
                              onCheckedChange = { /* Handle checkbox state change */ },
                          )
                          TextDynamic(
                              text = requiredText,
                              color = requireTextColor
                          )
                          TextDynamic(
                              text = policy.title,
                              color = MaterialTheme.colorScheme.primary,
                          )
                      }
                if (index < policyList.size - 1) {
                    Spacer(modifier = Modifier.height(Dimen.spaceM))
                }
            }
            val allAgreed = remember(policyList) {
                policyList.filter { it.required }.all { it.required }
            }
            allPolicyAgreed(allAgreed)
        }
    }
}

//@Preview(showBackground = true, apiLevel = 34)
//@Composable
//fun PolicyListPreview() {
//    PolicyList(
//        policyList = listOf(
//            PolicyItem(
//                id = "1",
//                title = "정책 1",
//                required = true,
//                files = listOf("file1.pdf"),
//                options = listOf("option1", "option2")
//            ),
//            PolicyItem(
//                id = "2",
//                title = "정책 2",
//                required = false,
//                files = listOf("file2.pdf"),
//                options = listOf("option3", "option4")
//            )
//        ),
//        onClick = {}
//    )
//}