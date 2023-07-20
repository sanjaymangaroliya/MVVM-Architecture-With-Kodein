package com.mvvmarchitecturewithkodein.ui

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvvmarchitecturewithkodein.R
import com.mvvmarchitecturewithkodein.api.ApiException
import com.mvvmarchitecturewithkodein.model.ProductModel
import com.mvvmarchitecturewithkodein.utils.NetUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductViewModel internal constructor(private val repository: ProductRepository) :
    ViewModel() {

    //MARK: Variable declaration
    var validationMessage = MutableLiveData<String?>()
    var progress = MutableLiveData<Boolean>()
    var apiFail = MutableLiveData<String?>()
    var productListResponse: MutableLiveData<ProductModel> = MutableLiveData<ProductModel>()

    //MARK: Get product list api call
    fun getProductList(context: Context) {
        if (NetUtils.isNetworkAvailable(context)) {
            progress.value = true
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val response = repository.getProductList()
                    withContext(Dispatchers.Main) {
                        progress.value = false
                        if (response.statusCode == 200) {
                            productListResponse.value = response

                        } else {
                            validationMessage.value = response.message
                        }
                    }
                } catch (e: ApiException) {
                    withContext(Dispatchers.Main) {
                        progress.value = false
                        apiFail.value = e.message
                    }
                }
            }
        } else {
            progress.value = false
            apiFail.value = context.getString(R.string.no_internet_connection)
        }
    }
}