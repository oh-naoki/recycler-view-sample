package com.example.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class CourseDetail (
    @PrimaryKey val id: String,
    val name: String,
    @ColumnInfo(name = "icon_url") val iconUrl: String,
    @ColumnInfo(name = "number_of_topics") val numberOfTopics: Int,
    @ColumnInfo(name = "teacher_name") val teacherName: String,
    val progress: Int
)