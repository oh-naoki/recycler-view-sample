package com.example.quipper_cording_test.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quipper_cording_test.R
import com.example.quipper_cording_test.ui.fragment.viewmodel.FavoriteCourseListViewModel
import com.example.quipper_cording_test.ui.view.CourseListAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_favorite_course_list.*
import org.koin.android.viewmodel.ext.android.viewModel

class FavoriteCourseListFragment : Fragment(){

    private val viewModel: FavoriteCourseListViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        lifecycle.addObserver(viewModel)
        return  inflater.inflate(R.layout.fragment_favorite_course_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favorite_course_list.layoutManager = LinearLayoutManager(requireActivity())

        viewModel.favoriteCourseList.observe(this, Observer { courseList ->
            favorite_course_list.adapter = CourseListAdapter(courseList).also { adapter ->
                adapter.favoriteStateObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        viewModel.changeFavoriteState(it)
                    }
            }
        })
    }
}