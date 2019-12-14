package com.danl.csmoneyapplication.model

import com.google.gson.annotations.SerializedName

class Player(
    @SerializedName("PlayerId") val id: Int
    , @SerializedName("Team") val team: Int
)