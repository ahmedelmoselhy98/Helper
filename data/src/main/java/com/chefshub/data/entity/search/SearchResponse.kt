package com.chefshub.data.entity.search

import com.chefshub.data.entity.tutorial.Chef
import com.chefshub.data.entity.tutorial.TutorialModel
import com.chefshub.data.entity.tutorial.TutorialVideos
import com.chefshub.data.entity.user.UserModel
import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("chefs") var chefs: ArrayList<UserModel> = arrayListOf(),
    @SerializedName("tutorials") var tutorials: ArrayList<TutorialModel> = arrayListOf(),
    @SerializedName("tutorialVideos") var tutorialVideos: ArrayList<TutorialVideos> = arrayListOf()
)

data class MostViewResponse(
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("is_following")
    var is_following: Boolean? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("avatar_path")
    var avatarPath: String? = null,
    @SerializedName("regional_cuisines_count")
    var regionalCuisinesCount: Int? = null,
    )

data class MealResponse(
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("logo_path")
    var logo_path: String? = null,
    @SerializedName("created_at")
    var created_at: String? = null,
    @SerializedName("length_in_minutes")
    var length_in_minutes: String? = null,
    @SerializedName("meal_type")
    var meal_type: String? = null,
    @SerializedName("main_video")
    var main_video: TutorialVideos? = null,
    @SerializedName("chef")
    var chef: Chef? = null,


//    @SerializedName("is_following")
//    var is_following: Boolean? = null,
//    @SerializedName("name")
//    var name: String? = null,
//    @SerializedName("avatar_path")
//    var avatarPath: String? = null,
//    @SerializedName("regional_cuisines_count")
//    var regionalCuisinesCount: Int? = null,

    )
