package com.mvvmarchitecturewithkodein.api

import android.content.Context
import com.mvvmarchitecturewithkodein.model.PostModel
import com.mvvmarchitecturewithkodein.model.ProductModel
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("products")
    suspend fun getProductList(): Response<ProductModel>


    /*@GET("getProductById")
    suspend fun getOrderHistory(
        @Header("Authorization") token: String,
        @Path("USER_ID") product_id: String,
    ): Response<ProductModel>

    @Multipart
    @POST("Order")
    suspend fun uploadProduct(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part,
        @PartMap map: Map<String, @JvmSuppressWildcards RequestBody>
    ): Response<ProductModel>


    @POST("addToFavourite")
    suspend fun addToFavourite(
        @Header("Authorization") token: String,
        @Body request: JsonObject
    ): Response<ProductModel>

    @GET("productList")
    suspend fun productList(
        @Header("Authorization") token: String,
        @QueryMap hashMap: HashMap<String, String>
    ): Response<ProductModel>*/

    companion object Factory {
        operator fun invoke(context: Context): ApiService {
            return RetrofitProvider.getRetrofitInstance(context).create(ApiService::class.java)
        }
    }
}