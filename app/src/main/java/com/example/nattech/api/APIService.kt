package com.example.nattech.api

import com.example.retrofit.api.Response
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface APIService {
    @GET("status")
    fun login(): Call<Response>

    @PUT("message?topic=movement")
    fun movement(@Body requestBody: RequestBody): Call<ResponseBody>

    @PUT("message?topic=rotary")
    fun shoveling(@Body requestBody: RequestBody): Call<ResponseBody>

    @PUT("message?topic=leveling")
    fun leveling(@Body requestBody: RequestBody): Call<ResponseBody>

    @PUT("message?topic=autonomous")
    fun autonomous(@Body requestBody: RequestBody): Call<ResponseBody>

    /*
    @PUT("message?topic=forward")
    fun forward(@Body requestBody: RequestBody): Call<ResponseBody>

    @PUT("message?topic=backward")
    fun backward(@Body requestBody: RequestBody): Call<ResponseBody>

    @PUT("message?topic=turnleft")
    fun turnleft(@Body requestBody: RequestBody): Call<ResponseBody>

    @PUT("message?topic=turnright")
    fun turnright(@Body requestBody: RequestBody): Call<ResponseBody>

     */

/*
    @Headers("Content-Type: text/plain")
    @PUT("message/{topic}/{status}")
    fun update(
        @Path("topic") topic: String,
        @Path("status") status: String
    ): Call<PublishResponse>
 */

    /*
    ใช้ไม่ได้
    @Headers("Content-Type: text/plain")
    @PUT("message/{topic}")
    fun update(
        @Path("topic") topic: PublishRequest,
        @Body publishRequest: PublishRequest
    ): Call<PublishResponse>

     */



}