package com.example.nattech.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BasicAuthClient<T> {
    private val baseURL = "https://api.netpie.io/v2/device/"
    private var clientID = "87fcc494-2f6b-440a-94db-1bdc649c05f4"
    private var token = "9eBh3JuYiTdtFPVEKr9VRyWk4LRAv1op"

    private val client =  OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(clientID, token))
        .build()

    private val gson = GsonBuilder()
        .setLenient()
        .create()


    private val retrofit = Retrofit.Builder()
        .baseUrl(baseURL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun create(service: Class<T>): T {
        return retrofit.create(service)
    }

}