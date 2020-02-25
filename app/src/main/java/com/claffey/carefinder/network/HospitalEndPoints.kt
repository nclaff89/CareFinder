package com.claffey.carefinder.network

import com.claffey.carefinder.models.Data
import com.claffey.carefinder.models.Hospital
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface HospitalEndPoints {
    @GET("hospitals/")
    fun getHosptialsAsync(@Header("x-access-token") token: String?): Deferred<Response<List<Hospital>>>

}