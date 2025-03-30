package com.miiiin15.myloan.list.presentation.screen.detail

import androidx.lifecycle.viewModelScope
import com.miiiin15.myloan.base.common.util.SharedPreferenceManager
import com.miiiin15.myloan.base.domain.result.Result
import com.miiiin15.myloan.base.presentation.nav.NavManager
import com.miiiin15.myloan.base.presentation.viewmodel.BaseViewModel
import com.miiiin15.myloan.base.presentation.viewmodel.BaseState
import com.miiiin15.myloan.base.presentation.viewmodel.BaseAction
import com.miiiin15.myloan.list.domain.model.ApplyUser
import com.miiiin15.myloan.list.domain.model.ProductDetail
import com.miiiin15.myloan.list.domain.repository.ApplyInfoRepository
import com.miiiin15.myloan.list.presentation.screen.detail.ProductDetailViewModel.UiState.*
import com.miiiin15.myloan.list.domain.usecase.GetProductDetailUseCase
import com.miiiin15.myloan.list.presentation.screen.mobileVerification.Action
import com.miiiin15.myloan.list.presentation.screen.mobileVerification.MobileVerificationFragmentDirections
import kotlinx.coroutines.launch
import java.util.UUID

internal class ProductDetailViewModel(
    private val navManager: NavManager,
    private val applyInfoRepository: ApplyInfoRepository,
    private val getProductDetailUseCase: GetProductDetailUseCase,
    private val sharedPreferenceManager: SharedPreferenceManager,
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
        val tokenTime  = sharedPreferenceManager.getString("accessToken")
        val currentTime = System.currentTimeMillis()
        val isOver12Hours = tokenTime?.let { currentTime - tokenTime.toLong() >= 12 * 60 * 60 * 1000 } ?: true
        if (isOver12Hours) {
            // 발급 시간 12시간 초과
            println("❌ 발급 시간 12시간 초과, 모바일 인증 화면으로 이동")
            navManager.navigate(ProductDetailFragmentDirections.actionProductDetailToMobileVerification())
        } else {
            // 발급 시간 12시간 미만
            println("⭕️ 발급 시간 12시간 미만, 대출 신청전 확인 화면으로 이동")
            navManager.navigate(ProductDetailFragmentDirections.actionProductDetailToLoanApplyPolicy())
        }
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