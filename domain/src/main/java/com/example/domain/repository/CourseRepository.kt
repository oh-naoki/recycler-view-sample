package com.example.domain.repository

import com.example.domain.model.CourseDetailWithFavorite
import com.example.domain.model.Favorite
import io.reactivex.Single

interface CourseRepository {
    fun getAllCourses(): Single<List<CourseDetailWithFavorite>>
    fun updateFavoriteState(favorite: Favorite): Single<Favorite>
    fun getAllOnlyFavorite(): Single<List<CourseDetailWithFavorite>>
    fun updateProgress(courseList: List<CourseDetailWithFavorite>): Single<List<CourseDetailWithFavorite>>
}