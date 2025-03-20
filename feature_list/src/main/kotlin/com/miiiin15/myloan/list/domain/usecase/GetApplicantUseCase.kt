package com.miiiin15.myloan.list.domain.usecase

import com.miiiin15.myloan.base.domain.result.Result
import com.miiiin15.myloan.list.domain.model.Applicant
import com.miiiin15.myloan.list.domain.repository.ApplicantRepository

internal class GetApplicantUseCase (
    private val applicantRepository: ApplicantRepository,
) {

    suspend operator fun invoke(): Result<Applicant> {
        return applicantRepository.getApplicantInfo()
    }
}