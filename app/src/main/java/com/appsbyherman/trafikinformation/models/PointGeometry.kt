package com.appsbyherman.trafikinformation.models

import com.google.gson.annotations.SerializedName

data class PointGeometry(
    @SerializedName("SWEREF99TM") val sweref99tm: String,
    @SerializedName("WGS84") val wgs84: String
)