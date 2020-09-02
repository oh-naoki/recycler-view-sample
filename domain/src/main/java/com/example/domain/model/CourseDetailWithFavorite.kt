package com.example.domain.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

@Entity
data class CourseDetailWithFavorite(
    @Embedded
    var courseDetail: CourseDetail,
    @Relation(parentColumn = "id", entityColumn = "course_id")
    var favorite: Favorite? = null
)