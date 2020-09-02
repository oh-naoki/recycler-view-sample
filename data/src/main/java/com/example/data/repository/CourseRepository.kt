package com.example.data.repository

import com.example.data.api.CordingTestApi
import com.example.data.db.database.CourseDataBase
import com.example.domain.model.CourseDetail
import com.example.domain.model.CourseDetailWithFavorite
import com.example.domain.model.Favorite
import com.example.domain.model.Usage
import com.example.domain.repository.CourseRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class CourseRepository(
    private val cordingTestApi: CordingTestApi,
    private val courseDataBase: CourseDataBase
) : CourseRepository {
    
    override fun getAllCourses(): Single<List<CourseDetailWithFavorite>> {
        return getAllCoursesByApi()
    }

    fun getAllCoursesByApi(): Single<List<CourseDetailWithFavorite>> {
        return cordingTestApi.getCourses()
            .flatMap {
                Observable.fromIterable(it)
                    .flatMapSingle { course ->
                        cordingTestApi.getUsage(course.id)
                            .onErrorReturn {
                                Usage(
                                    courseId = course.id,
                                    progress = 0
                                )
                            }
                            .zipWith(
                                courseDataBase.favoriteDao().findById(course.id)
                                    .onErrorReturn {
                                        Favorite(
                                            courseId = course.id,
                                            isFavorite = false
                                        )
                                    },
                                BiFunction{first: Usage, second: Favorite -> Pair(first, second)}
                            )
                            .map { pair ->
                                CourseDetailWithFavorite(
                                    CourseDetail(
                                        id = course.id,
                                        name = course.name,
                                        iconUrl = course.iconUrl,
                                        numberOfTopics = course.numberOfTopics,
                                        teacherName = course.teacherName,
                                        progress = pair.first.progress
                                    ),
                                    Favorite(
                                        courseId = course.id,
                                        isFavorite = pair.second.isFavorite
                                    )
                                )
                            }
                    }
                    .toList()
                    .doOnSuccess { result ->
                        courseDataBase.courseDetailDao()
                            .deleteAll()
                            .andThen(
                                courseDataBase.courseDetailDao().insertAll(result.map {
                                        courseDetailWithFavorite ->  courseDetailWithFavorite.courseDetail
                                }.toList())
                            ).subscribe()
                    }
            }.onErrorResumeNext {
                courseDataBase.courseDetailDao().getAll()
            }
    }

    override fun updateFavoriteState(favorite: Favorite): Single<Favorite> {
        val currentFavoriteState = Favorite(favorite.courseId, !favorite.isFavorite)
        return courseDataBase.favoriteDao()
            .insertAll(currentFavoriteState)
            .andThen(
                Single.just(currentFavoriteState)
            )
    }

    override fun getAllOnlyFavorite(): Single<List<CourseDetailWithFavorite>> {
        return courseDataBase.courseDetailDao().getAllOnlyFavorite()
    }

    override fun updateProgress(courseList: List<CourseDetailWithFavorite>): Single<List<CourseDetailWithFavorite>> {
        return Observable.fromIterable(courseList)
            .flatMapSingle { courseDetailWithFavorite ->
                cordingTestApi.getUsage(courseDetailWithFavorite.courseDetail.id)
                    .onErrorReturn {
                        Usage(
                            courseId = courseDetailWithFavorite.courseDetail.id,
                            progress = courseDetailWithFavorite.courseDetail.progress
                        )
                    }
                    .map {
                        with(courseDetailWithFavorite) {
                            CourseDetailWithFavorite(
                                courseDetail = CourseDetail(
                                    id = courseDetail.id,
                                    name = courseDetail.name,
                                    iconUrl = courseDetail.iconUrl,
                                    teacherName = courseDetail.teacherName,
                                    numberOfTopics = courseDetail.numberOfTopics,
                                    progress = it.progress
                                ),
                                favorite = favorite
                            )
                        }
                    }.flatMap {
                        courseDataBase.courseDetailDao().update(it.courseDetail)
                            .andThen(
                                Single.just(it)
                            )
                    }
            }
            .toList()
            .onErrorResumeNext {
                Single.just(courseList)
            }
    }
}