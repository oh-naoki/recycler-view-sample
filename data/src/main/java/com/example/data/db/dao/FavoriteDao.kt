package com.example.data.db.dao

import androidx.room.*
import com.example.domain.model.Favorite
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM Favorite WHERE course_id = :courseId")
    fun findById(courseId: String): Single<Favorite>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg favorite: Favorite): Completable

    @Update
    fun update(favorite: Favorite): Completable
}