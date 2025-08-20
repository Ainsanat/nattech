package com.example.azimuth

import com.google.android.gms.maps.model.LatLng

data class CoordinateItem(
    val imageLocation: Int,
    val title: String? = null
)
data class LocationItem(
    val latLng: LatLng
)
