package com.danl.csmoneyapplication

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.select_match_activity.*

class SelectMatchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.select_match_activity)
        title = "Выбор матча"
        match.setOnClickListener {
            startActivity(Intent(this, SelectRoundActivity::class.java))
        }
    }

}