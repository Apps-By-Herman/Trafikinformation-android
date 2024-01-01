package com.appsbyherman.trafikinformation.models

import com.google.gson.annotations.SerializedName

data class Schedule(
    @SerializedName("EndOfPeriod") val endOfPeriod: String,
    @SerializedName("RecurringTimePeriodOfDay") val recurringTimePeriodOfDay: RecurringTimePeriodOfDay,
    @SerializedName("StartOfPeriod ") val startOfPeriod : String,
)
