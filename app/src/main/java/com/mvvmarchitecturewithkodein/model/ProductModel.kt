package com.mvvmarchitecturewithkodein.model

data class ProductModel(
    val statusCode: Int? = 200,
    val message: String? = null,
    val products: ArrayList<ProductsItem?>? = null
)

data class ProductsItem(
    val thumbnail: String? = null,
    val images: ArrayList<String?>? = null,
    val price: Int? = null,
    val description: String? = null,
    val id: Int? = null,
    val title: String? = null,
    val stock: Int? = null,
    val rating: Float? = null,
    val category: String? = null,
    val brand: String? = null
)

