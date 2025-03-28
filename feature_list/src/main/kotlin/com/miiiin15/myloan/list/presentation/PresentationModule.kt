package com.miiiin15.myloan.list.presentation

import com.miiiin15.myloan.list.presentation.screen.list.ProductListViewmodel
import com.miiiin15.myloan.list.presentation.screen.detail.ProductDetailViewModel
import com.miiiin15.myloan.list.presentation.screen.mobileVerification.MobileVerificationViewModel
import com.miiiin15.myloan.list.presentation.screen.apply.policy.LoanApplyPolicyViewModel
import com.miiiin15.myloan.list.presentation.screen.apply.suitability.LoanSuitabilityViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val presentationModule = module {
    viewModelOf(::ProductListViewmodel)
    viewModelOf(::ProductDetailViewModel)
    viewModelOf(::MobileVerificationViewModel)
    viewModelOf(::LoanApplyPolicyViewModel)
    viewModelOf(::LoanSuitabilityViewModel)
}
