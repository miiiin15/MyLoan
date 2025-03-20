package com.miiiin15.myloan.list.data.datasource.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.miiiin15.myloan.list.domain.model.Applicant

@Entity(tableName = "applicant")
internal data class ApplicantEntityModel(
    @PrimaryKey(autoGenerate = true) val key: Int = 0,
    val id: String,
    val updateTime: Long,
)

internal fun ApplicantEntityModel.toDomainModel() =
    Applicant(
        this.id,
        this.updateTime,
    )

