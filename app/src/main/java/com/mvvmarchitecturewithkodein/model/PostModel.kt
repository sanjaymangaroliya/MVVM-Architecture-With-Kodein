package com.mvvmarchitecturewithkodein.model

data class PostModel(

	val postModel: ArrayList<PostModelItem?>? = null)

data class PostModelItem(
	val id: Int? = null,
	val title: String? = null,
	val body: String? = null,
	val userId: Int? = null
)

