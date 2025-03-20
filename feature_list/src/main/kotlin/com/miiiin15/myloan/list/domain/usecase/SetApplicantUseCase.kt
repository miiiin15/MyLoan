package com.miiiin15.myloan.list.domain.usecase

import com.miiiin15.myloan.base.domain.result.Result
import com.miiiin15.myloan.list.domain.repository.ApplicantRepository

internal class SetApplicantUseCase(
    private val applicantRepository: ApplicantRepository,
) {

    suspend operator fun invoke(id: String, updateTime: Long): Result<Boolean> {
        return applicantRepository.setApplicant(id, updateTime)
    }
}