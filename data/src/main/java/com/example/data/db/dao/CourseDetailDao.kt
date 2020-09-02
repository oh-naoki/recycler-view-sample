package com.example.data.db.dao

import androidx.room.*
import com.example.domain.model.CourseDetail
import com.example.domain.model.CourseDetailWithFavorite
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CourseDetailDao {
    @Query("SELECT CourseDetail.*, Favorite.* FROM CourseDetail LEFT JOIN Favorite ON CourseDetail.id = Favorite.course_id")
    fun getAll(): Single<List<CourseDetailWithFavorite>>

    @Query("SELECT CourseDetail.*, Favorite.* FROM CourseDetail JOIN Favorite ON CourseDetail.id = Favorite.course_id WHERE Favorite.is_favorite = 1")
    fun getAllOnlyFavorite(): Single<List<CourseDetailWithFavorite>>

    @Insert
    fun insertAll(courseDetailList: List<CourseDetail>): Completable

    @Update
    fun update(courseDetail: CourseDetail): Completable

    @Query("DELETE FROM CourseDetail")
    fun deleteAll(): Completable
}