package com.appsbyherman.trafikinformation.models

import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("HasFullSizePhoto") val hasFullSizePhoto: Boolean,
    @SerializedName("Url") val url: String,
)
