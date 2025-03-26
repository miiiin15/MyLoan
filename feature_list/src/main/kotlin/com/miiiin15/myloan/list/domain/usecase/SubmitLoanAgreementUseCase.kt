package com.miiiin15.myloan.list.domain.usecase

import com.miiiin15.myloan.base.domain.result.Result
import com.miiiin15.myloan.list.domain.model.apply.LoanApplyState
import com.miiiin15.myloan.list.domain.repository.ProductListRepository

class SubmitLoanAgreementUseCase(
    private val productListRepository: ProductListRepository,
) {
    suspend operator fun invoke(loanApplyState: LoanApplyState): Result<Unit> {
        return productListRepository.submitLoanAgreement(loanApplyState)
    }
}
