package com.miiiin15.myloan.list.data.datasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.miiiin15.myloan.list.data.datasource.database.model.ApplicantEntityModel

@Database(
    entities = [ApplicantEntityModel::class],
    version = 1,
    exportSchema = false
)
internal abstract class ApplicantDatabase : RoomDatabase() {
    abstract fun applicant(): ApplicantDao

}