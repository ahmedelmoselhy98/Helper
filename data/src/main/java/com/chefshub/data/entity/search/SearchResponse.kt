package com.chefshub.data.entity.search

import com.chefshub.data.entity.tutorial.TutorialModel
import com.chefshub.data.entity.tutorial.TutorialVideos
import com.chefshub.data.entity.user.UserModel
import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("chefs") var chefs: ArrayList<UserModel> = arrayListOf(),
    @SerializedName("tutorials") var tutorials: ArrayList<TutorialModel> = arrayListOf(),
    @SerializedName("tutorialVideos") var tutorialVideos: ArrayList<TutorialVideos> = arrayListOf()
)
