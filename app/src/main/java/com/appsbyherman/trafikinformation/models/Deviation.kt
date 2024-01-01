@file:Suppress("SpellCheckingInspection")

package com.appsbyherman.trafikinformation.models

import com.google.gson.annotations.SerializedName

data class Deviation(
    @SerializedName("AffectedDirection") val affectedDirection: String,
    @SerializedName("AffectedDirectionValue") val affectedDirectionValue: String,
    @SerializedName("CountryNo") val countyNo: List<Int>,
    @SerializedName("CreationTime") val creationTime: String,
    @SerializedName("Creator") val creator: String,
    @SerializedName("EndTime") val endTime: String,
    @SerializedName("Geometry") val geometry: Geometry,
    @SerializedName("Header") val header: String,
    @SerializedName("IconId") val iconId: String,
    @SerializedName("Image") val image: List<Image>,
    @SerializedName("HasFullSizePhoto") val hasFullSizePhoto: Boolean,
    @SerializedName("Url") val url: String,
    @SerializedName("JourneyReference") val journeyReference: String,
    @SerializedName("LocationDescriptor") val locationDescriptor: String,
    @SerializedName("ManagedCause") val managedCause: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("MessageCode") val messageCode: String,
    @SerializedName("MessageCodeValue") val messageCodeValue: String,
    @SerializedName("MessageType") val messageType: String,
    @SerializedName("MessageTypeValue") val messageTypeValue: String,
    @SerializedName("NumberOfLanesRestricted") val numberOfLanesRestricted: Int,
    @SerializedName("PositionalDescription") val positionalDescription: String,
    @SerializedName("RoadName") val roadName: String,
    @SerializedName("RoadNumber") val roadNumber: String,
    @SerializedName("RoadNumberNumeric") val roadNumberNumeric: Int,
    @SerializedName("SafetyRelatedMessage") val safetyRelatedMessage: Boolean,
    @SerializedName("Schedule") val schedule: List<Any>,
    @SerializedName("SeverityCode") val severityCode: Int,
    @SerializedName("SeverityText") val severityText: String,
    @SerializedName("StartTime") val startTime: String,
    @SerializedName("TemporaryLimit") val temporaryLimit: String,
    @SerializedName("TrafficRestrictionType") val trafficRestrictionType: String,
    @SerializedName("ValidUntilFurtherNotice") val validUntilFurtherNotice: Boolean,
    @SerializedName("WebLink") val webLink: String,
)
