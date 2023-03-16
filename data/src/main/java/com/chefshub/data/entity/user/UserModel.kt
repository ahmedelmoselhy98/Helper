package com.chefshub.data.entity.user

import com.chefshub.data.entity.tutorial.Chef
import com.google.gson.annotations.SerializedName

data class UserModel(

    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("is_following")
    var is_following: Boolean? = null,
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
    @SerializedName("social_media")
    var socialMedia: SocialMedia? = SocialMedia(),
    @SerializedName("food_systems")
    var foodSystems: ArrayList<String> = arrayListOf(),
    @SerializedName("regional_cuisines")
    var regionalCuisines: ArrayList<RegionalCuisines>  = arrayListOf(),
    @SerializedName("regional_cuisines_count")
    var regionalCuisinesCount: Int?=null,
    var token:String?=null
)

data class SocialMedia(
    @SerializedName("facebook") var facebook: String? = null,
    @SerializedName("youtube") var youtube: String? = null,
    @SerializedName("instagram") var instagram: String? = null,
)



data class RegionalCuisines(
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("slug")
    var slug: String? = null,

)
