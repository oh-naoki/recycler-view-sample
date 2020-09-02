package com.example.data.repository


import com.example.data.api.CordingTestApi
import com.example.data.db.database.CourseDataBase
import com.example.domain.model.*
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class CourseRepositoryTest: Spek({

    val cordingTestApi: CordingTestApi = mockk()
    val courseDataBase: CourseDataBase = mockk(relaxed = true)
    val courseRepository = CourseRepository(cordingTestApi, courseDataBase)

    val testCourses = listOf(
        Course("abc123","","",3, ""),
        Course("efg123","","",3, ""),
        Course("hij123","","",3, "")
    )
    val usage = Usage("", 100)
    val favorite = Favorite("abc123", true)
    val testCourseDetailWithFavorite = listOf(
        CourseDetailWithFavorite(
            courseDetail = CourseDetail("abc123","","",3, "", 10),
            favorite = favorite
        ),
        CourseDetailWithFavorite(
            courseDetail = CourseDetail("efg123","","",3, "", 30),
            favorite = favorite
        )
    )

    describe("CourseRepositoryTest"){

        beforeEachTest {
            clearAllMocks()
        }

        describe("invoke getAllCourses"){

            beforeEachTest {
                every { cordingTestApi.getCourses() } returns Single.just(testCourses)
                every { cordingTestApi.getUsage(any()) } returns Single.just(usage)
                every { courseDataBase.favoriteDao().findById(any()) } returns Single.just(favorite)
                every { courseDataBase.courseDetailDao().deleteAll() } returns Completable.complete()
                every { courseDataBase.courseDetailDao().insertAll(any()) } returns Completable.complete()
            }

            it("should call getCourses and call getUsage as mush as course list size"){
                courseRepository.getAllCoursesByApi().test()

                verify(exactly = 1) { cordingTestApi.getCourses() }
                verify(exactly = 1) { cordingTestApi.getUsage("abc123") }
                verify(exactly = 1) { cordingTestApi.getUsage("efg123") }
                verify(exactly = 1) { cordingTestApi.getUsage("hij123") }
                verify(exactly = 3) { courseDataBase.favoriteDao().findById(any()) }
            }
        }

        describe("invoke getAllCourses and getUsage returns Error"){
            it("should returns progress zero"){
                every { cordingTestApi.getCourses() } returns Single.just(testCourses)
                every { cordingTestApi.getUsage(any()) } returns Single.just(usage)
                every { cordingTestApi.getUsage("efg123") } returns Single.error(Exception())

                every { courseDataBase.favoriteDao().findById(any()) } returns Single.just(favorite)
                every { courseDataBase.favoriteDao().findById("abc123") } returns Single.error(Exception())

                every { courseDataBase.courseDetailDao().deleteAll() } returns Completable.complete()
                every { courseDataBase.courseDetailDao().insertAll(any()) } returns Completable.complete()

                val result = courseRepository.getAllCoursesByApi().test()

                result.assertNoErrors()

                assertThat(result.values()[0][0].courseDetail.progress).isEqualTo(usage.progress)
                assertThat(result.values()[0][1].courseDetail.progress).isEqualTo(0)
                assertThat(result.values()[0][2].courseDetail.progress).isEqualTo(usage.progress)

                assertThat(result.values()[0][0].favorite?.isFavorite).isFalse()
                assertThat(result.values()[0][1].favorite?.isFavorite).isTrue()
                assertThat(result.values()[0][2].favorite?.isFavorite).isTrue()
            }
        }

        describe("invoke getAllCourses returns Error"){
            it("should invoke courseDetailDao getAll"){
                every { cordingTestApi.getCourses() } returns Single.error(Exception())

                courseRepository.getAllCoursesByApi().test()

                verify(exactly = 1) { courseDataBase.courseDetailDao().getAll() }
            }
        }

        describe("invoke updateFavoriteState"){
            it("should return reverse favorite state"){
                every { courseDataBase.favoriteDao().insertAll(any()) } returns Completable.complete()

                val result = courseRepository.updateFavoriteState(favorite).test()

                verify(exactly = 1) { courseDataBase.favoriteDao().insertAll(any()) }

                assertThat(result.values()[0].courseId).isEqualTo(favorite.courseId)
                assertThat(result.values()[0].isFavorite).isFalse()
            }
        }

        describe("invoke getAllOnlyFavorite"){
            it("should invoke courseDetailDao getAllOnlyFavorite"){
                courseRepository.getAllOnlyFavorite().test()

                verify(exactly = 1) { courseDataBase.courseDetailDao().getAllOnlyFavorite() }
            }
        }

        describe("invoke updateProgress"){
            it("should update progress"){
                every { cordingTestApi.getUsage(any()) } returns Single.just(usage)
                every { courseDataBase.courseDetailDao().update(any()) } returns Completable.complete()

                val result = courseRepository.updateProgress(testCourseDetailWithFavorite).test()

                verify(exactly = 2) { cordingTestApi.getUsage(any()) }
                verify(exactly = 2) { courseDataBase.courseDetailDao().update(any()) }

                assertThat(result.values()[0][0].courseDetail.progress).isEqualTo(100)
                assertThat(result.values()[0][1].courseDetail.progress).isEqualTo(100)
            }
        }

        describe("invoke updateProgress and room returns error"){
            it("should returns same courseList"){
                every { cordingTestApi.getUsage(any()) } returns Single.just(usage)
                every { courseDataBase.courseDetailDao().update(any()) } returns Completable.error(Exception())

                val result = courseRepository.updateProgress(testCourseDetailWithFavorite).test()

                assertThat(result.values()[0]).isEqualTo(testCourseDetailWithFavorite)
            }
        }
    }
})