package com.mvvmarchitecturewithkodein.api

import com.mvvmarchitecturewithkodein.utils.Utils
import retrofit2.Response

abstract class SafeApiRequest {
    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>?): T {
        val response = call.invoke()
        if (response != null && response.isSuccessful && response.code() == 200) {
            return response.body()!!
        } else {
            throw ApiException(Utils.chkStr(response?.message()))
        }
    }
}

/*abstract class SafeApiRequest {
    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {
        val errorMessage = "Something went wrong, please try again."
        val underMaintenance = "Sorry for inconvenience. App is under maintenance."

        val response: Response<T>?
        try {
            response = call.invoke()
        } catch (e: IllegalStateException) {
            throw ApiException(errorMessage)
        } catch (e: JsonSyntaxException) {
            throw ApiException(errorMessage)
        } catch (e: Exception) {
            throw ApiException(errorMessage)
        }

        val code = response.code()
        if (response.isSuccessful && code == 200) {
            return response.body()!!
        } else if (code == 400) {
            throw ApiException(response.message())
        } else if (code == 401) {
            throw ApiException(response.message())
        } else if (code == 403) {
            throw ApiException(response.message())
        } else if (code == 500 || code == 503) {
            throw ApiException(underMaintenance)
        } else {
            throw ApiException(response.message())
        }
    }
}*/


