package com.example.data.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.db.dao.CourseDetailDao
import com.example.data.db.dao.FavoriteDao
import com.example.domain.model.CourseDetail
import com.example.domain.model.Favorite

@Database(
    entities = [CourseDetail::class, Favorite::class],
    version = 1
)
abstract class CourseDataBase : RoomDatabase(){
    abstract fun courseDetailDao(): CourseDetailDao
    abstract fun favoriteDao(): FavoriteDao
}