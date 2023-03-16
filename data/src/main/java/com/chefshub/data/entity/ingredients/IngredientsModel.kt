package com.chefshub.data.entity.ingredients

import com.google.gson.annotations.SerializedName

data class IngredientsModel(
    @SerializedName("name") var name: String? = null,
    @SerializedName("amount") var amount: Int? = null,
    @SerializedName("unit") var unit: String? = null,
    )
