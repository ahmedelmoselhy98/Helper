package com.chefshub.data.entity.user

import com.google.gson.annotations.SerializedName

data class AuthMeta(
    @SerializedName("message") var message: String? = null,
    @SerializedName("token") var token: String? = null,
    @SerializedName("signed_in") var signedIn: Boolean? = null


)
