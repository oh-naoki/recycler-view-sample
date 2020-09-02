package com.example.quipper_cording_test.ui.fragment.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.example.domain.model.CourseDetailWithFavorite
import com.example.domain.model.Favorite
import com.example.domain.repository.CourseRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class CourseListFragmentViewModel(
    private val courseRepository: CourseRepository
) : ViewModel(), LifecycleObserver{

    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    private val _courseList: MutableLiveData<List<CourseDetailWithFavorite>> = MutableLiveData()
    val courseList : LiveData<List<CourseDetailWithFavorite>>
        get() = _courseList

    private val _notifyItemChange: MutableLiveData<Int> = MutableLiveData()
    val notifyItemChange: LiveData<Int>
        get() = _notifyItemChange

    private val _progressVisibility: MutableLiveData<Int> = MutableLiveData()
    val progressVisibility: LiveData<Int>
        get() = _progressVisibility

    private val _notifyDataSetChanged: MutableLiveData<Unit> = MutableLiveData()
    val notifyDataSetChanged: LiveData<Unit>
        get() = _notifyDataSetChanged

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onCreate(){
        courseRepository.getAllCourses()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                _progressVisibility.value = View.VISIBLE
            }
            .doFinally {
                _progressVisibility.value = View.GONE
            }
            .subscribeBy (
                onSuccess = {
                    _courseList.value = it
                },
                onError = {
                    Log.e("Get All Courses Error", it.message)
                }
            ).addTo(compositeDisposable)
    }

    fun changeFavoriteState(position: Int){
        courseList.value?.let { courseList ->
            val previousState = courseList[position].favorite ?: Favorite(courseList[position].courseDetail.id, false)

            courseRepository.updateFavoriteState(previousState)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy (
                    onSuccess = {
                        courseList[position].favorite?.isFavorite = it.isFavorite
                        _notifyItemChange.value = position
                    },
                    onError = {
                        Log.e("Update Favorite Error", it.message)
                    }
                )
        }
    }

    fun swipeRefresh(){
        courseList.value?.let {
            courseRepository.updateProgress(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally {
                    _notifyDataSetChanged.value = Unit
                }
                .subscribeBy (
                    onSuccess = { result ->
                        _courseList.value = result
                    },
                    onError = {
                        Log.e("Update Progress Error", it.message)
                    }
                )
        }
    }
}