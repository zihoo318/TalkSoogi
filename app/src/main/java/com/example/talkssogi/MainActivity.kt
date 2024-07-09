package com.example.talkssogi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ////////////page9로 가기/////////
        val buttonOpenPage9 = findViewById<Button>(R.id.button_open_page9)
        buttonOpenPage9.setOnClickListener {
            val intent = Intent(this, fragmentPage9::class.java)
            startActivity(intent)
        }

        ////////////page7로 가기///////////
        val buttonOpenPage7 = findViewById<Button>(R.id.button_open_page7)
        buttonOpenPage7.setOnClickListener {
            val intent = Intent(this, FragmentActivity::class.java)
            startActivity(intent)
        }
        ////////////////////////////////
    }
}