package com.miiiin15.myloan.list.presentation.screen.list

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.common.collect.ImmutableList
import com.miiiin15.myloan.base.AppConfig
import com.miiiin15.myloan.base.domain.result.Result
import com.miiiin15.myloan.base.presentation.nav.NavManager
import com.miiiin15.myloan.base.presentation.viewmodel.BaseAction
import com.miiiin15.myloan.base.presentation.viewmodel.BaseState
import com.miiiin15.myloan.base.presentation.viewmodel.BaseViewModel
import com.miiiin15.myloan.list.domain.model.Product
import com.miiiin15.myloan.list.domain.usecase.GetApplicantUseCase
import com.miiiin15.myloan.list.domain.usecase.GetProductListUseCase
import com.miiiin15.myloan.list.domain.usecase.SetApplicantUseCase
import com.miiiin15.myloan.list.presentation.screen.list.ProductListViewmodel.UiState
import com.miiiin15.myloan.list.presentation.screen.list.ProductListViewmodel.UiState.Content
import com.miiiin15.myloan.list.presentation.screen.list.ProductListViewmodel.UiState.Error
import com.miiiin15.myloan.list.presentation.screen.list.ProductListViewmodel.UiState.Loading
import kotlinx.coroutines.launch
import java.util.UUID

internal class ProductListViewmodel(
    private val savedStateHandle: SavedStateHandle,
    private val navManager: NavManager,
    private val setApplicantUseCase: SetApplicantUseCase,
    private val getApplicantUseCase: GetApplicantUseCase,
    private val getProductListUseCase: GetProductListUseCase
) :
    BaseViewModel<UiState, ProductListViewmodel.Action>(
        Loading
    ) {
    var _isProductListFetched = false

    fun fetchApplicant() {
        if (savedStateHandle.get<Boolean>("isApplicantFetched") == true) return

        viewModelScope.launch {
            getApplicantUseCase().also { result ->
                when (result) {
                    is Result.Success -> {
                        AppConfig.applicantId = result.value.id
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

    fun fetchProductList() {
        if (_isProductListFetched) return

        viewModelScope.launch {
            getProductListUseCase().also { result ->
                val action = when (result) {
                    is Result.Success -> {
                        if (result.value.isEmpty()) {
                            Action.ProductListLoadFailure
                        } else {
                            _isProductListFetched = true
                            Action.ProductListLoadSuccess(ImmutableList.copyOf(result.value))
                        }
                    }

                    is Result.Failure -> {
                        Action.ProductListLoadFailure
                    }
                }
                sendAction(action)
            }
        }
    }

    fun onProductClick(productType: String) {
        val navDirections =
            ProductListFragmentDirections.actionProductListToProductDetail(productType)

        navManager.navigate(navDirections)
    }

    internal sealed interface Action : BaseAction<UiState> {
        class ProductListLoadSuccess(private val productList: ImmutableList<Product>) : Action {
            override fun reduce(state: UiState) = Content(productList)
        }


        object ProductListLoadFailure : Action {
            override fun reduce(state: UiState) = Error
        }
    }

    @Immutable
    internal sealed interface UiState : BaseState {
        data class Content(val productList: ImmutableList<Product>) : UiState
        object Loading : UiState
        object Error : UiState
    }
}