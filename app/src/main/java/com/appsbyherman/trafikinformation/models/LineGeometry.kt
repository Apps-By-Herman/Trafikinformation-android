package com.appsbyherman.trafikinformation.models

import com.google.gson.annotations.SerializedName

data class LineGeometry(
    @SerializedName("SWEREF99TM") val sweref99tm: String,
    @SerializedName("WGS84") val wgs84: String
)
