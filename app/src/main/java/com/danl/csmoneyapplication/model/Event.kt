package com.danl.csmoneyapplication.model

import com.google.gson.annotations.SerializedName

class Event(
    @SerializedName("FrameNumber") val frameNumber: Int, @SerializedName("EventType") val eventType: Int
    , @SerializedName("Data") val data: Data
)

class Data(@SerializedName("Player") val player: Int, @SerializedName("VictimPos") val victimPos: VictimPos, @SerializedName("IsHeadshot") val isHeadShot: Boolean) {

}

class VictimPos(@SerializedName("X") val x: Float, @SerializedName("Y") val y: Float)