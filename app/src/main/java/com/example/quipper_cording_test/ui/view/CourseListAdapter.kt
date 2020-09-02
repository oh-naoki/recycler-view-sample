package com.example.quipper_cording_test.ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.domain.model.CourseDetailWithFavorite
import com.example.domain.model.DisplayProgress
import com.example.quipper_cording_test.R
import com.example.quipper_cording_test.extension.formatNumOfTopics
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class CourseListAdapter(
    private val courseList: List<CourseDetailWithFavorite>
): RecyclerView.Adapter<CourseViewHolder>(){

    private val _favoriteStateSubject = PublishSubject.create<Int>()
    val favoriteStateObservable: Observable<Int>
        get() = _favoriteStateSubject.hide()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.course_item, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val data = courseList[position]
        with(holder){
            courseIcon.load(data.courseDetail.iconUrl)
            courseName.text = data.courseDetail.name
            teacherName.text = data.courseDetail.teacherName
            numberOfTopics.text = data.courseDetail.numberOfTopics.formatNumOfTopics()
            progressText.text = DisplayProgress.fromProgress(data.courseDetail.progress).status
            favoriteStatus.setImageResource(setFavoriteIcon(data.favorite?.isFavorite))

            favoriteStatus.setOnClickListener { _favoriteStateSubject.onNext(position) }
        }
    }

    override fun getItemCount() = courseList.size

    private fun setFavoriteIcon(isFavorite: Boolean?) = when(isFavorite){
        true -> R.drawable.ic_favorite
        else -> R.drawable.ic_not_favorite
    }
}

