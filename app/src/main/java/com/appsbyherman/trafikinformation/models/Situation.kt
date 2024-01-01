@file:Suppress("SpellCheckingInspection")

package com.appsbyherman.trafikinformation.models

import com.google.gson.annotations.SerializedName

data class Situation (
    @SerializedName("Id") val id: String,
    @SerializedName("Deleted") val deleted: Boolean,
    @SerializedName("Deviation") val deviation: List<Deviation>,
    @SerializedName("CountryCode") val countryCode: String,
    @SerializedName("VersionTime") val versionTime: String,
    @SerializedName("ModifiedTime") val modifiedTime: String,
    @SerializedName("PublicationTime") val publicationTime: String
)
