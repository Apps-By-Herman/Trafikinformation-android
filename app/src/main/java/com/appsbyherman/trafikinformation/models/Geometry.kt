package com.appsbyherman.trafikinformation.models

import com.google.gson.annotations.SerializedName

data class Geometry(
    @SerializedName("Line") val line: LineGeometry,
    @SerializedName("Point") val point: PointGeometry
)

