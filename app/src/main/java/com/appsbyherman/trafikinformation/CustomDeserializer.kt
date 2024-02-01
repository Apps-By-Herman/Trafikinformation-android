package com.appsbyherman.trafikinformation

import com.appsbyherman.trafikinformation.models.Deviation
import com.appsbyherman.trafikinformation.models.Geometry
import com.appsbyherman.trafikinformation.models.Image
import com.appsbyherman.trafikinformation.models.LineGeometry
import com.appsbyherman.trafikinformation.models.ParentResponse
import com.appsbyherman.trafikinformation.models.PointGeometry
import com.appsbyherman.trafikinformation.models.RecurringTimePeriodOfDay
import com.appsbyherman.trafikinformation.models.Response
import com.appsbyherman.trafikinformation.models.Result
import com.appsbyherman.trafikinformation.models.Schedule
import com.appsbyherman.trafikinformation.models.Situation
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class CustomDeserializer : JsonDeserializer<Response> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?,
                             context: JsonDeserializationContext?): Response {

        var trafikverketResult = mutableListOf(Result(listOf()))

        val jsonSituations = json
            ?.asJsonObject
            ?.getAsJsonObject("RESPONSE")
            ?.getAsJsonArray("RESULT")
            ?.firstOrNull()?.asJsonObject
            ?.getAsJsonArray("Situation")
            ?: return Response(trafikverketResult)

        val situations = mutableListOf<Situation>()

        for (situation in jsonSituations) {

            val jsonObject = situation.asJsonObject

            // Only value that should not be null...
            val id = jsonObject.get("Id").asString
            val deleted = jsonObject.get("Deleted")?.asBoolean ?: false

            // Deviation
            val deviations = mutableListOf<Deviation>()
            val deviation = jsonObject.get("Deviation")?.asJsonArray
            deviation?.forEach { dev ->
                val deviationJsonObject = dev.asJsonObject
                val affectedDirection = deviationJsonObject.get("AffectedDirection")?.asString ?: ""
                val affectedDirectionValue = deviationJsonObject.get("AffectedDirectionValue")?.asString ?: ""
                val countyNo = deviationJsonObject.get("CountryNo")?.asJsonArray?.map { it.asInt } ?: listOf()
                val creationTime = deviationJsonObject.get("CreationTime")?.asString ?: ""
                val creator = deviationJsonObject.get("Creator")?.asString ?: ""
                val endTime = deviationJsonObject.get("EndTime")?.asString ?: ""

                val geometry = deviationJsonObject.get("Geometry")?.asJsonObject?.let {
                    val line = it.get("Line")?.asJsonObject
                    val point = it.get("Point")?.asJsonObject
                    Geometry(
                        LineGeometry(
                            line?.get("SWEREF99TM")?.asString ?: "",
                            line?.get("WGS84")?.asString ?: ""
                        ),
                        PointGeometry(
                            point?.get("SWEREF99TM")?.asString ?: "",
                            point?.get("WGS84")?.asString ?: ""
                        )
                    )
                } ?: Geometry(LineGeometry("", ""), PointGeometry("", ""))

                val header = deviationJsonObject.get("Header")?.asString ?: ""
                val iconId = deviationJsonObject.get("IconId")?.asString ?: ""
                val image = deviationJsonObject.get("Image")?.asJsonArray?.map {
                    val imageJsonObject = it.asJsonObject
                    val hasFullSizePhoto = imageJsonObject.get("HasFullSizePhoto")?.asBoolean ?: false
                    val url = imageJsonObject.get("Url")?.asString ?: ""
                    Image(hasFullSizePhoto, url)
                } ?: listOf()

                val hasFullSizePhoto = deviationJsonObject.get("HasFullSizePhoto")?.asBoolean ?: false
                val url = deviationJsonObject.get("Url")?.asString ?: ""
                val journeyReference = deviationJsonObject.get("JourneyReference")?.asString ?: ""
                val locationDescriptor = deviationJsonObject.get("LocationDescriptor")?.asString ?: ""
                val managedCause = deviationJsonObject.get("ManagedCause")?.asBoolean ?: false
                val message = deviationJsonObject.get("Message")?.asString ?: ""
                val messageCode = deviationJsonObject.get("MessageCode")?.asString ?: ""
                val messageCodeValue = deviationJsonObject.get("MessageCodeValue")?.asString ?: ""
                val messageType = deviationJsonObject.get("MessageType")?.asString ?: ""
                val messageTypeValue = deviationJsonObject.get("MessageTypeValue")?.asString ?: ""
                val numberOfLanesRestricted = deviationJsonObject.get("NumberOfLanesRestricted")?.asInt ?: 0
                val positionalDescription = deviationJsonObject.get("PositionalDescription")?.asString ?: ""
                val roadName = deviationJsonObject.get("RoadName")?.asString ?: ""
                val roadNumber = deviationJsonObject.get("RoadNumber")?.asString ?: ""
                val roadNumberNumeric = deviationJsonObject.get("RoadNumberNumeric")?.asInt ?: 0
                val safetyRelatedMessage = deviationJsonObject.get("SafetyRelatedMessage")?.asBoolean ?: false

                val schedule = deviationJsonObject.get("Schedule")?.asJsonArray?.map {
                    val scheduleJsonObject = it.asJsonObject
                    val endOfPeriod = scheduleJsonObject.get("EndOfPeriod")?.asString ?: ""
                    val recurringTimePeriodOfDay = scheduleJsonObject.get("RecurringTimePeriodOfDay")?.asJsonArray?.map { rTPD ->
                        val recurringTimePeriodOfDayJsonObject = rTPD.asJsonObject
                        val end = recurringTimePeriodOfDayJsonObject.get("End")?.asString ?: ""
                        val start = recurringTimePeriodOfDayJsonObject.get("Start")?.asString ?: ""
                        RecurringTimePeriodOfDay(end, start)
                    } ?: listOf()
                    val startOfPeriod = scheduleJsonObject.get("StartOfPeriod")?.asString ?: ""

                    Schedule(endOfPeriod, recurringTimePeriodOfDay, startOfPeriod)
                } ?: listOf()

                val severityCode = deviationJsonObject.get("SeverityCode")?.asInt ?: 0
                val severityText = deviationJsonObject.get("SeverityText")?.asString ?: ""
                val startTime = deviationJsonObject.get("StartTime")?.asString ?: ""
                val temporaryLimit = deviationJsonObject.get("TemporaryLimit")?.asString ?: ""
                val trafficRestrictionType = deviationJsonObject.get("TrafficRestrictionType")?.asString ?: ""
                val validUntilFurtherNotice = deviationJsonObject.get("ValidUntilFurtherNotice")?.asBoolean ?: false
                val webLink = deviationJsonObject.get("WebLink")?.asString ?: ""

                deviations.add(Deviation(affectedDirection, affectedDirectionValue, countyNo,
                    creationTime, creator, endTime, geometry, header, iconId, image, hasFullSizePhoto,
                    url, journeyReference, locationDescriptor, managedCause, message, messageCode,
                    messageCodeValue, messageType, messageTypeValue, numberOfLanesRestricted,
                    positionalDescription, roadName, roadNumber, roadNumberNumeric, safetyRelatedMessage,
                    schedule, severityCode, severityText, startTime, temporaryLimit, trafficRestrictionType,
                    validUntilFurtherNotice, webLink))
            }

            val countryCode = jsonObject.get("CountryCode")?.asString ?: ""
            val versionTime = jsonObject.get("VersionTime")?.asString ?: ""
            val modifiedTime = jsonObject.get("ModifiedTime")?.asString ?: ""
            val publicationTime = jsonObject.get("PublicationTime")?.asString ?: ""

            situations.add(
                Situation(
                    id,
                    deleted,
                    deviations,
                    countryCode,
                    versionTime,
                    modifiedTime,
                    publicationTime,
                )
            )
        }

        trafikverketResult = mutableListOf(Result(situations))
        return Response(trafikverketResult)
    }
}
