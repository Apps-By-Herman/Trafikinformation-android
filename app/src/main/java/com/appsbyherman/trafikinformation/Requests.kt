package com.appsbyherman.trafikinformation

import android.util.Log
import com.appsbyherman.trafikinformation.models.Situation
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.osmdroid.util.GeoPoint

class Requests {
    companion object {
        fun getSituations(geoPoint: GeoPoint? = null,
                          distanceToGeoPointInMeters: Int? = 10000,
                          countyCode: Int? = null): List<Situation> {

            val builder = RequestBuilder.buildService()

            val requestBodyText = if (geoPoint != null) {
                val lat = geoPoint.latitude
                val lng = geoPoint.longitude

                """
                    <REQUEST>
                        <LOGIN authenticationkey="3dd9644ce90a4edfa86108ce0935831f" />
                      <QUERY objecttype="Situation" schemaversion="1.5">
                        <FILTER>
                            <NEAR name="Deviation.Geometry.Line.WGS84" value="$lng $lat" maxdistance="$distanceToGeoPointInMeters" />
                        </FILTER>
                      </QUERY>
                    </REQUEST>
                """.trimIndent()
            }
            else if (countyCode != null) {
                """
                    <REQUEST>
                        <LOGIN authenticationkey="3dd9644ce90a4edfa86108ce0935831f" />
                      <QUERY objecttype="Situation" schemaversion="1.5">
                        <FILTER>
                            <EQ name="Deviation.CountyNo" value="$countyCode" />
                        </FILTER>
                      </QUERY>
                    </REQUEST>
                """.trimIndent()
            }
            else {
                """
                    <REQUEST>
                        <LOGIN authenticationkey="3dd9644ce90a4edfa86108ce0935831f" />
                      <QUERY objecttype="Situation" schemaversion="1.5">
                        <FILTER>
                        </FILTER>
                      </QUERY>
                    </REQUEST>
                """.trimIndent()
            }

            val requestBody = requestBodyText.toRequestBody("application/xml".toMediaType())

            val call = builder.endpoint(requestBody)

            val rawResponse = try {
                call.execute()
            } catch (e: Exception) {
                Log.d("RequestsClass", e.stackTraceToString())
                return emptyList()
            }

            if (!rawResponse.isSuccessful) {
                Log.d("RequestsClass", rawResponse.errorBody()?.string().toString())
                return emptyList()
            }

            val response = rawResponse.body() ?: return emptyList()

            return response.result.firstOrNull()?.situations ?: emptyList()
        }
    }
}
