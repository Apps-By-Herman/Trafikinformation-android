package com.appsbyherman.trafikinformation

import org.osmdroid.util.GeoPoint

class Helpers {
    companion object {
        fun getGeoPoint(wgS84: String): GeoPoint {
            val raw = wgS84.substringAfter('(').substringBefore(')')
            val values = raw.split(' ')

            val long = values.first().toDouble()
            val lat = values.last().toDouble()

            return GeoPoint(lat, long)
        }

        fun getWGS84(geoPoint: GeoPoint): String {
            return "(${geoPoint.longitude} ${geoPoint.latitude})"
        }

        fun moveGeoPoint(geoPoint: GeoPoint, metersNorth: Double, metersEast: Double): GeoPoint {
            val degreesNorth = metersNorth / 111139
            val degreesEast = metersEast / 111139

            return GeoPoint(geoPoint.latitude + degreesNorth, geoPoint.longitude + degreesEast)
        }
    }
}