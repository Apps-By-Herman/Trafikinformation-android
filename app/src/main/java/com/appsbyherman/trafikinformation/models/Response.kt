package com.appsbyherman.trafikinformation.models

import com.google.gson.annotations.SerializedName

data class Response (
    @SerializedName("RESULT" )
    val result : List<Result>
)
