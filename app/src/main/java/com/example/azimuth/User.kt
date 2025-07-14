package com.example.azimuth

data class User(var id: String, var name: String? = null, var bio: String? = null) {
    constructor() : this("", "", "")
}
