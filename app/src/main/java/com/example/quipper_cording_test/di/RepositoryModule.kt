package com.example.quipper_cording_test.di

import com.example.data.repository.CourseRepository
import org.koin.core.module.Module
import org.koin.dsl.module

class RepositoryModule {

    val module: Module = module {
        single {
            CourseRepository(get(), get())
        }
    }
}