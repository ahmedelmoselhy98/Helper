package com.chefshub.data.entity.user

import com.google.gson.annotations.SerializedName

data class AddFavorite(

    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("meta") var meta: Meta? = null

)

data class Meta (
    @SerializedName("message") var message: String? = null,
)

