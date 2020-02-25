package com.claffey.carefinder.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetroFitInstance {

    companion object {
        /**
         * This was a locally hosted mongo DB on my macbook
         * For my client/server final project
         */
        //val API_URL = "http://localhost:3000/api/"
        val API_URL = "http://10.0.2.2:3000/api/"


        private var INSTANCE: Retrofit? = null

        fun getRetroFitInstance(): Retrofit {
            return INSTANCE ?: Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
        }
    }

}