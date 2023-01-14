package com.chefshub.data.entity.food_system

import com.google.gson.annotations.SerializedName

data class FoodSystemModel(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("slug") var slug: String? = null
)
