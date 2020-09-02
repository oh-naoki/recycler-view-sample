package com.example.quipper_cording_test.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quipper_cording_test.R
import com.example.quipper_cording_test.databinding.FragmentCourseListBinding
import com.example.quipper_cording_test.ui.fragment.viewmodel.CourseListFragmentViewModel
import com.example.quipper_cording_test.ui.view.CourseListAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_course_list.*
import org.koin.android.viewmodel.ext.android.viewModel

class CourseListFragment : Fragment(){

    private val viewModel: CourseListFragmentViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        lifecycle.addObserver(viewModel)
        val binding = DataBindingUtil.inflate<FragmentCourseListBinding>(
            inflater,
            R.layout.fragment_course_list,
            container,
            false
        ).apply {
            viewModel = this@CourseListFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        course_list.layoutManager = LinearLayoutManager(requireActivity())

        with(viewModel){
            courseList.observe(this@CourseListFragment, Observer { courseList ->
                course_list.adapter = CourseListAdapter(courseList).also { adapter ->
                    adapter.favoriteStateObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            viewModel.changeFavoriteState(it)
                        }
                }
            })

            notifyItemChange.observe(this@CourseListFragment, Observer {
                course_list.adapter?.notifyItemChanged(it)
            })

            notifyDataSetChanged.observe(this@CourseListFragment, Observer {
                course_list.adapter?.notifyDataSetChanged()
                swipeRefresh.isRefreshing = false
            })
        }

        swipeRefresh.setOnRefreshListener {
            viewModel.swipeRefresh()
        }
    }
}