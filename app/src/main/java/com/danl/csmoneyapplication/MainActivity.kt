package com.danl.csmoneyapplication

import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.danl.csmoneyapplication.model.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startButton.setOnClickListener {
            roundView.start()
        }
        stopButton.setOnClickListener {
            roundView.stop()
        }
        frameTimeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                PreferenceManager.getDefaultSharedPreferences(this@MainActivity).edit()
                    .putInt("frame_time", frameTimeSeekBar.progress).apply()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        val gson = Gson()
        val json = String(assets.open("round.json").readBytes())
        val jsonObject = gson.fromJson(json, JsonObject::class.java)
        val retrofit =
            Retrofit.Builder().baseUrl("http://193.187.175.194:8080/").addConverterFactory(
                GsonConverterFactory.create()
            ).build()
        retrofit.create(CsMoneyService::class.java).listPlayers().enqueue(object :
            Callback<List<Player>> {
            override fun onFailure(call: Call<List<Player>>, t: Throwable) {
                println(t)
            }

            override fun onResponse(call: Call<List<Player>>, response: Response<List<Player>>) {
                println(response)
                if (response.isSuccessful) {
                    val players = response.body() ?: return
                    roundView.players = players
                }
            }

        })
        val playerPositions: List<PlayerPosition> = gson.fromJson(
            jsonObject.getAsJsonArray("PlayersPositions"),
            object : TypeToken<List<PlayerPosition>>() {}.type
        )
        roundView.playerPositions = playerPositions
//        players = gson.fromJson(
//            jsonObject.getAsJsonArray("Players"),
//            object : TypeToken<List<Player>>() {}.type
//        )
        val bombPosition = gson.fromJson(
            jsonObject.getAsJsonArray("BombPositions").get(0).asJsonObject,
            BombPosition::class.java
        )
        roundView.bombPosition = bombPosition
        val events: List<Event> = gson.fromJson(
            jsonObject.getAsJsonArray("Events"),
            object : TypeToken<List<Event>>() {}.type
        )
        roundView.events = events
        val roundEndState = gson.fromJson(
            jsonObject.getAsJsonObject("RoundEndState"),
            RoundEndState::class.java
        )
        roundView.roundEndState = roundEndState

        statisticsButton.setOnClickListener {
            startActivity(Intent(this, StatisticsActivity::class.java))
        }
    }
}
