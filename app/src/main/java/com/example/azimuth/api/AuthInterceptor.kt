package com.example.azimuth.api

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(username: String, password: String) : Interceptor {
    private var credentials: String = "$username:$password"

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder()
            .addHeader("Authorization", "device $credentials")
            .addHeader("Content-Type", "text/plain")
            .build()

        return chain.proceed(request)
    }
}