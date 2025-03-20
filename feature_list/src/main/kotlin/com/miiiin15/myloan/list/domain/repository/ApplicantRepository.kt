package com.miiiin15.myloan.list.domain.repository

import com.miiiin15.myloan.list.domain.model.Applicant
import com.miiiin15.myloan.base.domain.result.Result

internal interface ApplicantRepository {

    suspend fun getApplicantInfo(): Result<Applicant>

    suspend fun setApplicant(id: String, updateTime: Long): Result<Boolean>
}