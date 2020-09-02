package com.example.data.api

import com.example.domain.model.Course
import com.example.domain.model.Usage
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface CordingTestApi {
    @GET("api/courses")
    fun getCourses(): Single<List<Course>>

    @GET("api/{course_id}/usage")
    fun getUsage(@Path("course_id") courseId: String): Single<Usage>
}