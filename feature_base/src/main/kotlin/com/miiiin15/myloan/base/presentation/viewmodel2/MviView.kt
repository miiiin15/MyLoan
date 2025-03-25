package com.miiiin15.myloan.base.presentation.viewmodel2

import androidx.annotation.CheckResult
import kotlinx.coroutines.flow.Flow

interface MviView<I : MviIntent, S : MviViewState, E : MviSingleEvent> {
    fun render(viewState: S)
    fun handleSingleEvent(event: E)
    @CheckResult
    fun viewIntents(): Flow<I>
}