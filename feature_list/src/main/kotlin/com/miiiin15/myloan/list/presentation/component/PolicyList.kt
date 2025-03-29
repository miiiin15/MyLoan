package com.miiiin15.myloan.list.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import com.miiiin15.myloan.base.common.res.Dimen
import com.miiiin15.myloan.base.presentation.compose.composable.TextDynamic
import com.miiiin15.myloan.base.presentation.compose.composable.TextType
import com.miiiin15.myloan.list.domain.model.PolicyItem


@Composable
fun PolicyList(
    policyList: List<PolicyItem>,
    modifier: Modifier = Modifier,
    onAllPolicyAgreed: (List<String>, Boolean) -> Unit,
    onClick: () -> Unit = {}
) {

    val policyCheckStates =
        remember(policyList) { mutableStateListOf(*Array(policyList.size) { false }) }
    val requiredIndexes =
        policyList.mapIndexedNotNull { idx, item -> if (item.required) idx else null }
    val allRequiredChecked = requiredIndexes.all { policyCheckStates[it] }

    fun setAllRequired(checked: Boolean) {
        policyCheckStates.indices.forEach { policyCheckStates[it] = checked }
    }

    // 필수만 검사해서 콜백
    // TODO : 선택 약관 트리거 점검
    LaunchedEffect(allRequiredChecked) {
        val checkedIds = policyList
            .mapIndexedNotNull { idx, item -> if (policyCheckStates[idx]) item.id else null }
        onAllPolicyAgreed(checkedIds, allRequiredChecked)
    }

    val outlineColor = MaterialTheme.colorScheme.outline

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onPrimary)
            .drawBehind {
                drawLine(
                    color = outlineColor,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = Dimen.spaceS.value
                )
            }
            .clickable { onClick() },
    ) {

        Column(
            horizontalAlignment = Alignment.Start,

            ) {
            // 전체동의 Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(Dimen.spaceM)
                    .height(Dimen.spaceXL)
                    .clickable {
                        setAllRequired(!allRequiredChecked)
                    }
            ) {
                Checkbox(
                    checked = allRequiredChecked,
                    modifier = Modifier.padding(end = Dimen.spaceM),
                    onCheckedChange = null
                )
                TextDynamic(
                    text = "전체동의",
                    type = TextType.BodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(Dimen.spaceM))

            // 개별 정책 Row
            policyList.forEachIndexed { index, policy ->
                val requiredText = if (policy.required) "[필수] " else "[선택] "
                val requireTextColor = if (policy.required) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.primary
                }
                key(policy.id) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                bottom = Dimen.spaceM,
                                start = Dimen.spaceM,
                                end = Dimen.spaceM
                            )
                            .height(Dimen.spaceXL)
                            .clickable { policyCheckStates[index] = !policyCheckStates[index] }
                    ) {
                        Checkbox(
                            checked = policyCheckStates[index],
                            modifier = Modifier.padding(end = Dimen.spaceS),
                            onCheckedChange = null
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
            }
        }
    }
}
