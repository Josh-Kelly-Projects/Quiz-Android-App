package com.example.testing

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class NotificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notifications)

        val containerLayout = findViewById<LinearLayout>(R.id.linearLayoutInside)

        val notiListLayout = LayoutInflater.from(this).inflate(R.layout.noti_list, containerLayout, false)

        val textViewName = notiListLayout.findViewById<TextView>(R.id.textViewNotName)
        textViewName.text = "Quiz night"

        val textViewDate = notiListLayout.findViewById<TextView>(R.id.textViewDate)
        textViewDate.text = "12/12/24 | 14:00"

        val textViewDetails = notiListLayout.findViewById<TextView>(R.id.textViewDetails)
        textViewDetails.text = "Hosting a quiz night on the geography in South Africa"

        containerLayout.addView(notiListLayout)

        val profileSetting = findViewById<ImageButton>(R.id.profileButton)
        profileSetting.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        val home = findViewById<ImageButton>(R.id.homeButton)
        home.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
        }

        val notification = findViewById<ImageButton>(R.id.notificationButton)
        notification.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }

        val menu = findViewById<ImageButton>(R.id.barButton)
        menu.setOnClickListener {
            val intent = Intent(this, BarActivity::class.java)
            startActivity(intent)
        }
    }
}