package com.example.domain.model

import com.google.gson.annotations.SerializedName

data class Usage(
    @SerializedName("course_id") val courseId: String,
    val progress: Int
)