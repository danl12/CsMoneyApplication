package com.danl.csmoneyapplication.model

import com.google.gson.annotations.SerializedName

class PlayerPosition(
    @SerializedName("X") val xArray: FloatArray
    , @SerializedName("Y") val yArray: FloatArray
    , @SerializedName("PlayerID") val playerId: Int
    , @SerializedName("ViewX") val directions: FloatArray
)