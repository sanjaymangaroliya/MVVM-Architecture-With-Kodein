package com.mvvmarchitecturewithkodein.application

import androidx.multidex.MultiDexApplication
import com.mvvmarchitecturewithkodein.api.ApiService
import com.mvvmarchitecturewithkodein.global.GlobalPref
import com.mvvmarchitecturewithkodein.ui.ProductFactory
import com.mvvmarchitecturewithkodein.ui.ProductRepository
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class MyApplication : MultiDexApplication(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@MyApplication))

        bind() from singleton { ApiService(this@MyApplication) }

        //MARK: All Repository
        bind() from singleton { ProductRepository(instance()) }

        //MARK: All Factory
        bind() from provider { ProductFactory(instance()) }
    }


    override fun onCreate() {
        super.onCreate()
        GlobalPref.initialize(this)
    }
}