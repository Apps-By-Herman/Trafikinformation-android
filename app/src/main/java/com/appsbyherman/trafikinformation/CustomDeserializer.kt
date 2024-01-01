package com.appsbyherman.trafikinformation

import com.appsbyherman.trafikinformation.models.Deviation
import com.appsbyherman.trafikinformation.models.ParentResponse
import com.appsbyherman.trafikinformation.models.Response
import com.appsbyherman.trafikinformation.models.Result
import com.appsbyherman.trafikinformation.models.Situation
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class CustomDeserializer : JsonDeserializer<ParentResponse> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?,
                             context: JsonDeserializationContext?): ParentResponse {

        var trafikverketResult = mutableListOf(Result(listOf()))

        val jsonSituations = json
            ?.asJsonObject
            ?.getAsJsonObject("RESPONSE")
            ?.getAsJsonArray("RESULT")
            ?.firstOrNull()?.asJsonObject
            ?.getAsJsonArray("Situation")
            ?: return ParentResponse(Response(trafikverketResult))

        val situations = mutableListOf<Situation>()

        for (situation in jsonSituations) {

            val jsonObject = situation.asJsonObject

            // Only value that should not be null...
            val id = jsonObject.get("Id").asString
            val deleted = jsonObject.get("Deleted")?.asBoolean ?: false

            // TODO: Continue here.........

            val deviations = mutableListOf<Deviation>()
            val deviation = jsonObject.get("Deviation")?.asJsonArray
            deviation?.forEach {
                val equipmentJsonObject = it.asJsonObject
                val type = equipmentJsonObject.get("Type")?.asString ?: ""
                val accessibility = equipmentJsonObject.get("Accessibility")?.asString ?: ""
                deviations.add(Equipment(type, accessibility))
            }

            val locationDescription = jsonObject.get("LocationDescription")?.asString ?: ""
            val description = jsonObject.get("Description")?.asString ?: ""

            val facilities = mutableListOf<Facility>()
            val facility = jsonObject.get("Facility")?.asJsonArray
            facility?.forEach {
                val facilityJsonObject = it.asJsonObject
                val type = facilityJsonObject.get("Type")?.asString ?: ""
                facilities.add(Facility(type))
            }

            val iconId = jsonObject.get("IconId")?.asString ?: ""
            val wGS84 = jsonObject.get("Geometry")?.asJsonObject?.get("WGS84")?.asString ?: ""
            val name = jsonObject.get("Name")?.asString ?: ""

            val photos = mutableListOf<Photo>()
            val photo = jsonObject.get("Photo")?.asJsonArray
            photo?.forEach {
                val photoJsonObject = it.asJsonObject
                val title = photoJsonObject.get("Title")?.asString ?: ""
                val url = photoJsonObject.get("Url")?.asString ?: ""
                photos.add(Photo(title, url))
            }

            val vehicleCharacteristics = mutableListOf<VehicleCharacteristics>()
            val vehicleCharacteristicsJson = jsonObject.get("VehicleCharacteristics")?.asJsonArray
            vehicleCharacteristicsJson?.forEach {
                val vehicleCharacteristicsJsonObject = it.asJsonObject
                val vehicleType = vehicleCharacteristicsJsonObject.get("VehicleType")?.asString ?: ""
                val numberOfSpaces = vehicleCharacteristicsJsonObject.get("NumberOfSpaces")?.asInt ?: 0
                vehicleCharacteristics.add(VehicleCharacteristics(vehicleType, numberOfSpaces))
            }
            situations.add(
                Situation(
                    id,
                    deleted,
                )
            )
        }

        trafikverketResult = mutableListOf(Result(situations))
        return ParentResponse(Response(trafikverketResult))
    }
}
