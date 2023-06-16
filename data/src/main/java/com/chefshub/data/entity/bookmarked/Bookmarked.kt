package com.chefshub.data.entity.bookmarked

import com.google.gson.annotations.SerializedName

data class Bookmarked(

    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("meta") var meta: Meta? = null,
    @SerializedName("data") var data: ArrayList<VideoModel>? = null
)

class VideoModel(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("tutorial_ id") var tutorial_id: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("caption") var caption: String? = null,
    @SerializedName("url") var url: String? = null,
    @SerializedName("screenshot_url") var screenshot_url: String? = null,
    @SerializedName("main") var main: Boolean? = null,

    @SerializedName("created_at") var createdAt: String? = null,
)

data class Meta (
    @SerializedName("message") var message: String? = null,
)

