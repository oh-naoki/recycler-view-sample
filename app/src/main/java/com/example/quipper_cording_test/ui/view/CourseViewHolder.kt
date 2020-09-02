package com.example.quipper_cording_test.ui.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.course_item.view.*

class CourseViewHolder(view: View) : RecyclerView.ViewHolder(view){
    val courseIcon = view.course_icon
    val courseName = view.course_name
    val teacherName = view.teacher_name
    val numberOfTopics = view.number_of_topics
    val progressText = view.progress_text
    val favoriteStatus = view.favorite_status
}