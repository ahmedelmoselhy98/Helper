package com.chefshub.data.remote

import android.net.Uri
import com.chefshub.data.entity.EndPointModel
import com.chefshub.data.entity.food_system.FoodSystemModel
import com.chefshub.data.entity.tutorial.TutorialVideos
import com.chefshub.data.entity.user.AddFavorite
import com.chefshub.data.entity.user.AuthMeta
import com.chefshub.data.entity.user.UserModel
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun loginWithSocial(
        @Field("provider_id") provider_id: String,
        @Field("provider_name") provider_name: String,
        @Field("name") name: String,
        @Field("avatar_path") avatar_path: String,
        @Field("device_token") device_token: String,
        @Field("device_id") device_id: String,
    ): Response<EndPointModel<UserModel, AuthMeta>>

    @FormUrlEncoded
    @POST("auth/logout")
    suspend fun logout(
        @Field("device_token") device_token: String,
        @Field("device_id") device_id: String,
    ): Response<EndPointModel<Any, Any>>

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun loginWithEmail(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("device_token") device_token: String,
        @Field("device_id") device_id: String,
    ): Response<EndPointModel<UserModel, AuthMeta>>


    @FormUrlEncoded
    @POST("auth/login")
    suspend fun signup(
        @Field("email") email: String,
        @Field("name") name: String,
        @Field("password") password: String,
        @Field("device_token") device_token: String,
        @Field("device_id") device_id: String,
    ): Response<EndPointModel<UserModel, AuthMeta>>


    @FormUrlEncoded
    @POST("profile/update")
    suspend fun updateProfile(
        @Field("email") email: String,
        @Field("name") name: String,
        @Field("password") password: String,
        @Field("avatar_path") avatar_path: String,
    ): Response<EndPointModel<UserModel, AuthMeta>>


    @GET("auth/me")
    suspend fun getProfile(): Response<EndPointModel<UserModel, Any>>

    @GET("chefs/{id}")
    suspend fun getUser(@Path("id") id: Int): Response<EndPointModel<UserModel, Any>>

    @GET("data/food-systems")
    suspend fun getFoodSystem(): Response<EndPointModel<ArrayList<FoodSystemModel>, Any>>

    @GET("data/regional-cuisines")
    suspend fun getRegionalCuisines(): Response<EndPointModel<ArrayList<FoodSystemModel>, Any>>

    @POST("chefs/{id}/follow")
    suspend fun toggleFollow(
        @Path("id") id: Int): Response<EndPointModel<Any, Any>>

    @GET("tutorials/1/videos/{id}?is_following=1")
    suspend fun singleVideo(
        @Path("id") id: Int): Response<EndPointModel<TutorialVideos, Any>>

    @POST("videos/{id}/favourites")
    suspend fun addFavorite(@Path("id") id: Int): Response<EndPointModel<Any, Any>>

    @POST("videos/{id}/bookmark")
    suspend fun addSavedVideo(@Path("id") id: Int): Response<EndPointModel<Any, Any>>

}