package com.example.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favorite(
    @PrimaryKey @ColumnInfo(name = "course_id") val courseId: String,
    @ColumnInfo(name = "is_favorite") var isFavorite: Boolean
)