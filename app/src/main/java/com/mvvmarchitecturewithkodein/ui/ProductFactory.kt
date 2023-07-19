package com.mvvmarchitecturewithkodein.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProductFactory(private val repository: ProductRepository) :
    ViewModelProvider.NewInstanceFactory() {

    //MARK: ProductViewModel instance create
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProductViewModel(repository) as T
    }

}