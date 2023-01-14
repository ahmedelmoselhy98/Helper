package com.chefshub.data.entity.user

import com.google.gson.annotations.SerializedName

data class UserModel(

    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("posts_count")
    var postsCount: Int? = null,
    @SerializedName("followers_count")
    var followersCount: Int? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("bio")
    var bio: String? = null,
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("avatar_path")
    var avatarPath: String? = null,
    @SerializedName("food_systems")
    var foodSystems: ArrayList<String> = arrayListOf(),
    @SerializedName("regional_cuisines")
    var regionalCuisines: ArrayList<String> = arrayListOf(),
    var token:String?=null

)
