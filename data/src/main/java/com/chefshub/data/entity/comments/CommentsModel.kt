package com.chefshub.data.entity.comments

import com.google.gson.annotations.SerializedName

data class CommentsModel(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("body") var body: String? = null,
    @SerializedName("creator") var creator: Creator = Creator(),
//    @SerializedName("id") var id: Int? = null,
//    @SerializedName("id") var id: Int? = null,
//    @SerializedName("id") var id: Int? = null,
)

data class Creator(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("avatar_path") var avatarPath: String? = null,
    @SerializedName("is_following") var isFollowing: Boolean? = null,

)
