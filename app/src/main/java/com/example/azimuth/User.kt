package com.example.azimuth

data class User(
    var profile: Profile? = null,
    var device: Device? = null,
    var location: Location? = null
) {
    constructor() : this(null, null, null)
}

data class Profile(
    var name: String? = null,
    var email: String? = null,
    var phone: String? = null,
    var bio: String? = null,
) {
    constructor() : this("", "", "", "")
}

data class Device(
    val id: String? = null,
    val name: String? = null,
    var clientID: String? = null,
    var token: String? = null,
    var status: String? = null,
    var streamingURI: String? = null,
    var description: String? = null
) {
    constructor() : this("", "", "", "", "", "", "")
}

data class Location(
    var locationName: String? = null,
    var coordinateSet: String? = null,
    var imageCapture: String? = null
) {
    constructor() : this("", "", "")
}