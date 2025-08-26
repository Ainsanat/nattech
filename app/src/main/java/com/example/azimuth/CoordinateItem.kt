package com.example.azimuth

import com.google.android.gms.maps.model.LatLng

data class CoordinateItem(
    val imageLocation: Int,
    val title: String? = null
)
data class LocationItem(
    val latLng: LatLng
)
data class StateDeviceItem(
    val nameDevice: String? = null,
    val battery: String? = null,
    val velocity: String? = null,
    val temperature: String? = null

)
