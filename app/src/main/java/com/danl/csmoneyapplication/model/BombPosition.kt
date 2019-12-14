package com.danl.csmoneyapplication.model

import com.google.gson.annotations.SerializedName

class BombPosition(
    @SerializedName("X") val x: FloatArray, @SerializedName("Y") val y: FloatArray, @SerializedName(
        "StartFrame"
    ) val startFrame: Int, @SerializedName("EndFrame") val endFrame: Int
) {
}