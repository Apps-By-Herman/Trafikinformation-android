package com.appsbyherman.trafikinformation

import com.appsbyherman.trafikinformation.models.ParentResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface TrafikverketEndpoints {
    @POST("v2/data.json/")
    fun endpoint(@Body body: RequestBody): Call<ParentResponse>
}