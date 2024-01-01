package com.appsbyherman.trafikinformation.models

import com.google.gson.annotations.SerializedName

data class Result (
    @SerializedName("Situation" )
    val situations : List<Situation>
)
