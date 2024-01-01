package com.appsbyherman.trafikinformation.models

import com.google.gson.annotations.SerializedName

data class RecurringTimePeriodOfDay(
    @SerializedName("End") val end: String,
    @SerializedName("Start") val start: String,
)
