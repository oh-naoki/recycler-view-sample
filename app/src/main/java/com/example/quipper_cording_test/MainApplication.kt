package com.example.quipper_cording_test

import android.app.Application
import com.example.quipper_cording_test.di.*
import com.facebook.stetho.Stetho
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import retrofit2.Retrofit

class MainApplication : Application(){

    override fun onCreate() {
        super.onCreate()

        startKoin()

        Stetho.initialize(
            Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build())
    }

    private fun startKoin(){
        startKoin {
            androidContext(this@MainApplication)
            modules(
                listOf(
                    HttpClientModule().module,
                    RestClientModule().module,
                    ApiModule().module,
                    RepositoryModule().module,
                    DataBaseModule().module,
                    ViewModelModule().module
                )
            )
        }
    }
}