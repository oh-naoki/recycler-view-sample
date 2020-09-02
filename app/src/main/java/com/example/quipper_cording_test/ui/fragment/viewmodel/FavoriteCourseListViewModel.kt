package com.example.quipper_cording_test.ui.fragment.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.domain.model.CourseDetailWithFavorite
import com.example.domain.model.Favorite
import com.example.domain.repository.CourseRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class FavoriteCourseListViewModel(
    private val courseRepository: CourseRepository
) : ViewModel(), LifecycleObserver{

    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    private val _favoriteCourseList: MutableLiveData<List<CourseDetailWithFavorite>> = MutableLiveData()
    val favoriteCourseList : LiveData<List<CourseDetailWithFavorite>>
        get() = _favoriteCourseList

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onCreate(){
        courseRepository.getAllOnlyFavorite()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (
                onSuccess = {
                    _favoriteCourseList.value = it
                },
                onError = {
                    Log.e("Get All Courses Error", it.message)
                }
            ).addTo(compositeDisposable)
    }

    fun changeFavoriteState(position: Int){
        favoriteCourseList.value?.let { favoriteCourseList ->
            val previousState = favoriteCourseList[position].favorite ?: Favorite(favoriteCourseList[position].courseDetail.id, false)

            courseRepository.updateFavoriteState(previousState)
                .flatMap {
                    courseRepository.getAllOnlyFavorite()
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy (
                    onSuccess = {
                        _favoriteCourseList.value = it
                    },
                    onError = {
                        Log.e("Update or Get List Err", it.message)
                    }
                )
        }
    }
}