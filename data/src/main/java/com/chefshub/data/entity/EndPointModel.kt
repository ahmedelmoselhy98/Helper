package com.chefshub.data.entity

import com.google.gson.annotations.SerializedName

data class EndPointModel<T, M>(
    @SerializedName("data") var data: T? = null,
    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("meta") var meta: M? = null
)


