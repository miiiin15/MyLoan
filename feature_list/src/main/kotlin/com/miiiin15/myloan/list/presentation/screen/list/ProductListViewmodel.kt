package com.miiiin15.myloan.list.presentation.screen.list

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.miiiin15.myloan.base.domain.result.Result
import com.miiiin15.myloan.base.presentation.viewmodel.BaseAction
import com.miiiin15.myloan.base.presentation.viewmodel.BaseState
import com.miiiin15.myloan.base.presentation.viewmodel.BaseViewModel
import com.miiiin15.myloan.list.domain.usecase.GetApplicantUseCase
import com.miiiin15.myloan.list.domain.usecase.SetApplicantUseCase
import kotlinx.coroutines.launch
import java.util.UUID

internal class ProductListViewmodel(
    private val savedStateHandle: SavedStateHandle,
    private val setApplicantUseCase: SetApplicantUseCase,
    private val getApplicantUseCase: GetApplicantUseCase
) :
    BaseViewModel<ProductListViewmodel.UiState, ProductListViewmodel.Action>(
        UiState.Loading
    ) {


    fun fetchApplicant() {
        if (savedStateHandle.get<Boolean>("isApplicantFetched") == true) return

        viewModelScope.launch {
            getApplicantUseCase().also { result ->
                when (result) {
                    is Result.Success -> {
                        // TODO: 신청자 번호 후처리
                    }

                    is Result.Failure -> {
                        if (result.throwable is IllegalStateException) {
                            setApplicantUseCase(
                                UUID.randomUUID().toString().substring(0, 8),
                                System.currentTimeMillis()
                            )
                        }
                    }
                }
            }
            savedStateHandle["isApplicantFetched"] = true // 실행 여부 저장
        }
    }

    internal sealed interface Action : BaseAction<UiState> { }

    @Immutable
    internal sealed interface UiState : BaseState {
        object Loading : UiState
        object Error : UiState
    }
}