package com.appsbyherman.trafikinformation.models

import com.google.gson.annotations.SerializedName

data class ParentResponse (
  @SerializedName("RESPONSE" )
  val response : Response
)
