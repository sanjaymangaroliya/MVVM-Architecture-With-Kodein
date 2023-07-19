package com.mvvmarchitecturewithkodein.ui

import com.mvvmarchitecturewithkodein.api.ApiService
import com.mvvmarchitecturewithkodein.api.SafeApiRequest
import com.mvvmarchitecturewithkodein.model.PostModel
import com.mvvmarchitecturewithkodein.model.ProductModel

class ProductRepository(apiService: ApiService) : SafeApiRequest() {

    //MARK: Variable declaration
    private var apiService: ApiService

    init {
        this.apiService = apiService
    }

    //MARK: Get product list api call
    suspend fun getProductList(): ProductModel {
        return apiRequest { apiService.getProductList() }
    }
}