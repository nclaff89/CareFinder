package com.claffey.carefinder.network

import com.claffey.carefinder.models.AuthToken
import com.claffey.carefinder.models.User
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface LoginEndPoints {
    @FormUrlEncoded
    @POST("users/login")
    fun loginAsync(
        @Field(value = "email") email: String,
        @Field(value = "password") password: String
    ): Deferred<Response<AuthToken>>

    @FormUrlEncoded
    @POST("users/register")
    fun registerAsync(
        @Field(value = "firstName") firstName: String = "TestName",
        @Field(value = "lastName") lastName: String = "TestName",
        @Field(value = "email") email: String,
        @Field(value = "password") password: String,
        @Field(value = "admin") admin: Boolean



    ): Deferred<Response<AuthToken>>

    @GET("users/me")
    fun getRemoteUserAsync(@Header("x-access-token") token: String): Deferred<Response<User>>

    @GET("users/me")
    fun getRemoteUserStatusAsync(@Header("x-access-token") token: String): Deferred<Response<User>>
}