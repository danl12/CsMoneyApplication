package com.danl.csmoneyapplication

import com.danl.csmoneyapplication.model.Player
import retrofit2.Call
import retrofit2.http.GET

interface CsMoneyService  {

    @GET("api/v1/players")
    fun listPlayers(): Call<List<Player>>

}