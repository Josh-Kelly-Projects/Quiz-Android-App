package com.example.testing

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testing.session.PreferencesHelper

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome)

        val preferencesHelper = PreferencesHelper(applicationContext)

        //Name
        var guestName = "Welcome Guest"
        if (preferencesHelper.isLoggedIn()) {
            guestName = preferencesHelper.getPreferredName().toString()
            if (preferencesHelper.isAdmin()) {
                val containerLayout = findViewById<LinearLayout>(R.id.buttonEnterCreate)
                val buttonLayout = LayoutInflater.from(this).inflate(R.layout.admin_button, containerLayout, false)
                val textView = buttonLayout.findViewById<TextView>(R.id.buttonCreate)
                textView.text = "Create new quiz"
                containerLayout.addView(buttonLayout)

                textView.setOnClickListener {
                    val intent = Intent(this, CreateQuizActivity::class.java)
                    startActivity(intent)
                }

            }
        }
        val textViewName = findViewById<TextView>(R.id.textViewName)
        textViewName.text = guestName

        val codeButton = findViewById<Button>(R.id.buttonEnterCode)
        codeButton.setOnClickListener {
            val quizID = findViewById<EditText>(R.id.editTextCode).text.toString().toInt()
            preferencesHelper.setCurrentQuizId(quizID)
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)

        }

        val logOutButton = findViewById<Button>(R.id.buttonBack)
        logOutButton.setOnClickListener {
            preferencesHelper.logout()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

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