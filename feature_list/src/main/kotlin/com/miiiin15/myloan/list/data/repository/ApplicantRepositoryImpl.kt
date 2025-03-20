package com.miiiin15.myloan.list.data.repository

import com.miiiin15.myloan.list.data.datasource.database.ApplicantDao
import com.miiiin15.myloan.list.domain.model.Applicant
import com.miiiin15.myloan.list.domain.repository.ApplicantRepository
import com.miiiin15.myloan.base.domain.result.Result
import com.miiiin15.myloan.list.data.datasource.database.model.toDomainModel

internal class ApplicantRepositoryImpl(
    private val applicantDao: ApplicantDao,
) : ApplicantRepository {

    override suspend fun getApplicantInfo(): Result<Applicant> = runCatching {
        applicantDao.getApplicant().toDomainModel()
    }.fold(
        onSuccess = { Result.Success(it) },
        onFailure = { Result.Failure(it) }
    )

    override suspend fun setApplicant(id: String, updateTime: Long): Result<Boolean> = runCatching {
        applicantDao.insertApplicant(id, updateTime)
    }.fold(
        onSuccess = { Result.Success(true) },
        onFailure = { Result.Failure(it) }
    )
}