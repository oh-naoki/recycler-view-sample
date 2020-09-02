package com.example.quipper_cording_test.di

import com.example.data.repository.CourseRepository
import com.example.quipper_cording_test.ui.fragment.viewmodel.CourseListFragmentViewModel
import com.example.quipper_cording_test.ui.fragment.viewmodel.FavoriteCourseListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

class ViewModelModule {

    val module: Module = module{
        viewModel {
            CourseListFragmentViewModel(get<CourseRepository>())
        }
        viewModel {
            FavoriteCourseListViewModel(get<CourseRepository>())
        }
    }
}