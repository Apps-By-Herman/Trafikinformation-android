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
    }
}