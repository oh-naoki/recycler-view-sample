package com.example.domain.model

import com.google.gson.annotations.SerializedName

data class Course(
    val id: String,
    val name: String,
    @SerializedName("icon_url") val iconUrl: String,
    @SerializedName("number_of_topics") val numberOfTopics: Int,
    @SerializedName("teacher_name") val teacherName: String
)