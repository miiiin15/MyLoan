package com.miiiin15.myloan.list.data.datasource.database

import androidx.room.Dao
import androidx.room.Query
import com.miiiin15.myloan.list.data.datasource.database.model.ApplicantEntityModel

@Dao
internal interface ApplicantDao {

    @Query("SELECT * FROM applicant")
    suspend fun getApplicant(): ApplicantEntityModel

    @Query("INSERT OR REPLACE INTO applicant (id, updateTime) VALUES (:id, :updateTime)")
    suspend fun insertApplicant(id: String, updateTime: Long)

}