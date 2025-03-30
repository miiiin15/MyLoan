package com.miiiin15.myloan.list.presentation.screen.detail

import androidx.lifecycle.viewModelScope
import com.miiiin15.myloan.base.domain.result.Result
import com.miiiin15.myloan.base.presentation.nav.NavManager
import com.miiiin15.myloan.base.presentation.viewmodel.BaseViewModel
import com.miiiin15.myloan.base.presentation.viewmodel.BaseState
import com.miiiin15.myloan.base.presentation.viewmodel.BaseAction
import com.miiiin15.myloan.list.domain.model.ProductDetail
import com.miiiin15.myloan.list.domain.repository.ApplyInfoRepository
import com.miiiin15.myloan.list.presentation.screen.detail.ProductDetailViewModel.UiState.*
import com.miiiin15.myloan.list.domain.usecase.GetProductDetailUseCase
import kotlinx.coroutines.launch

internal class ProductDetailViewModel(
    private val navManager: NavManager,
    private val applyInfoRepository: ApplyInfoRepository,
    private val getProductDetailUseCase: GetProductDetailUseCase,
) : BaseViewModel<ProductDetailViewModel.UiState, ProductDetailViewModel.Action>(Loading) {

    init {
        applyInfoRepository.clearApplyInfo()
    }

    fun fetchProductDetail(productType: String) {
        viewModelScope.launch {
            getProductDetailUseCase(productType).also { result ->
                val action = when (result) {
                    is Result.Success -> {
                        applyInfoRepository.setApplyInfo("productType", productType)
                        Action.ProductDetailFetchSuccess(result.value)
                    }

                    is Result.Failure -> {
                        Action.ProductDetailFetchError
                    }
                }
                sendAction(action)
            }
        }
    }

    fun onApplyButtonClick() {
        val navDirection = ProductDetailFragmentDirections.actionProductDetailToMobileVerification()
        navManager.navigate(navDirection)
    }

    fun clear() {
        sendAction(Action.ProductDetailClearState)
    }

    internal sealed interface Action : BaseAction<UiState> {
        class ProductDetailFetchSuccess(val productDetail: ProductDetail) : Action {
            override fun reduce(state: UiState) = DetailContent(productDetail)
        }

        object ProductDetailFetchError : Action {
            override fun reduce(state: UiState) = Error
        }

        object ProductDetailClearState : Action {
            override fun reduce(state: UiState) = Loading
        }
    }

    internal sealed interface UiState : BaseState {
        data class DetailContent(val productDetail: ProductDetail) : UiState
        object Loading : UiState
        object Error : UiState
        // TODO: 필요한 상태 추가
    }
}