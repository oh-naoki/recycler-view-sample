package com.example.quipper_cording_test.di

import com.example.data.api.CordingTestApi
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

class ApiModule{

    val module: Module = module {

        single {
            get<Retrofit>().create(CordingTestApi::class.java)
        }
    }
}