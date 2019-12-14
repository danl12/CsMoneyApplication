package com.danl.csmoneyapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.select_round_activity.*

class SelectRoundActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.select_round_activity)
        round.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

}