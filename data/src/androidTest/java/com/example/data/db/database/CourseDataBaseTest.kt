package com.example.data.db.database

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import com.example.data.db.dao.CourseDetailDao
import com.example.data.db.dao.FavoriteDao
import com.example.domain.model.CourseDetail
import com.example.domain.model.Favorite
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat

class CourseDataBaseTest {

    private lateinit var db: CourseDataBase
    private lateinit var courseDetailDao: CourseDetailDao
    private lateinit var favoriteDao: FavoriteDao

    private val courseDetail = listOf(
        CourseDetail("1", "TestCourse", "Hoge",1,"Mike", 1),
        CourseDetail("2", "TestCourse2", "Fuga",5,"Ichiro", 30)
    )
    private val favoriteForCourse1 = Favorite("1", true)
    private val favoriteForCourse2 = Favorite("2", false)

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            CourseDataBase::class.java
        ).build()

        courseDetailDao = db.courseDetailDao()
        favoriteDao = db.favoriteDao()
    }

    @Test
    fun should_get_2_course_detail(){
        courseDetailDao.insertAll(courseDetail).test()

        val result = courseDetailDao.getAll().test()

        assertThat(result.values()[0].size).isEqualTo(2)
    }

    @Test
    fun should_get_1_favorite(){
        favoriteDao.insertAll(favoriteForCourse1).test()

        val result = favoriteDao.findById(favoriteForCourse1.courseId).test()

        assertThat(result.valueCount()).isEqualTo(1)
    }

    @Test
    fun should_get_1_course_detail_with_favorite(){
        courseDetailDao.insertAll(courseDetail).test()
        favoriteDao.insertAll(favoriteForCourse1).test()
        favoriteDao.insertAll(favoriteForCourse2).test()

        val result = courseDetailDao.getAllOnlyFavorite().test()

        assertThat(result.values()[0].size).isEqualTo(1)
    }

    @Test
    fun should_update_course_detail_progress(){
        val beforeCourseDetail = CourseDetail("1", "TestCourse", "Hoge",1,"Mike", 1)
        val before = courseDetailDao.insertAll(listOf(beforeCourseDetail)).andThen(courseDetailDao.getAll()).test().values()[0]

        val afterCourseDetail = CourseDetail("1", "TestCourse", "Hoge",1,"Mike", 30)
        val after = courseDetailDao.update(afterCourseDetail).andThen(courseDetailDao.getAll()).test().values()[0]

        assertThat(before.size).isEqualTo(1)
        assertThat(after.size).isEqualTo(1)

        assertThat(after.first().courseDetail.id).isEqualTo("1")
        assertThat(after.first().courseDetail.progress).isEqualTo(30)
    }

    @Test
    fun should_delete_all_course_detail(){
        val before = courseDetailDao.insertAll(courseDetail).andThen(courseDetailDao.getAll()).test().values()[0]
        val after = courseDetailDao.deleteAll().andThen(courseDetailDao.getAll()).test().values()[0]

        assertThat(before.size).isEqualTo(2)
        assertThat(after.size).isEqualTo(0)
    }

    @Test
    fun should_update_favorite_status(){
        val before = favoriteDao.insertAll(Favorite("1", false)).andThen(favoriteDao.findById("1")).test().values()[0]
        val after = favoriteDao.update(Favorite("1", true)).andThen(favoriteDao.findById("1")).test().values()[0]

        assertThat(before.isFavorite).isFalse()
        assertThat(after.isFavorite).isTrue()
    }
}