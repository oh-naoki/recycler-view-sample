package com.example.quipper_cording_test.di

import androidx.room.Room
import com.example.data.db.database.Constants.DB_NAME
import com.example.data.db.database.CourseDataBase
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module

class DataBaseModule {

    val module: Module = module {
        single {
            Room.databaseBuilder(androidApplication(), CourseDataBase::class.java, DB_NAME).build()
        }
    }
}