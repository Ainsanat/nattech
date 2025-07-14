package com.example.retrofit.api

data class Response(
    val deviceid: String,
    val alias: String,
    val groupid: String,
    val projectid: String,
    val status: Int,
    val enabled: Boolean,
    val banned: Boolean,
    val message: String,
    val topic: String
)

data class PublishRequest(val topic: String)

data class PublishResponse(val status: String, val message: String)