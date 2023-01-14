package com.chefshub.data.entity.tutorial

import com.google.gson.annotations.SerializedName

data class TutorialModel(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("chef") var chef: Chef? = Chef(),
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("length_in_minutes") var lengthInMinutes: Int? = null,
    @SerializedName("videos_count") var videosCount: Int? = null,
    @SerializedName("url") var url: String? = null,
    @SerializedName("caption") var caption: String? = null,


    @SerializedName("chef_id") var chefId: Int? = null,
    @SerializedName("slug") var slug: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("logo_path") var logoPath: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("image_path") var imagePath: String? = null,

    )

data class Chef(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("avatar_path") var avatarPath: String? = null

)

data class TutorialVideos(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("tutorial_id") var tutorialId: Int? = null,
    @SerializedName("uuid") var uuid: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("caption") var caption: String? = null,
    @SerializedName("processed") var processed: Int? = null,
    @SerializedName("enc_video_id") var encVideoId: String? = null,
    @SerializedName("enc_video_filename") var encVideoFilename: String? = null,
    @SerializedName("processed_percentage") var processedPercentage: String? = null,
    @SerializedName("length_in_seconds") var lengthInSeconds: Int? = null,
    @SerializedName("views") var views: Int? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("favourites_count") var favouritesCount: Int? = null,
    @SerializedName("comments_count") var commentsCount: Int? = null,
    @SerializedName("url") var url: String? = null,
    @SerializedName("chef") var chef: Chef? = Chef(),
    @SerializedName("shares_count") var sharesCount: Int? =null,
)