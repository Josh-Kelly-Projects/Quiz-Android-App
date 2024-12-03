package com.example.testing

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.testing.database.DatabaseHelper
import com.example.testing.session.PreferencesHelper

class CreateQuizFormActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var preferencesHelper: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_quiz_form)

        dbHelper = DatabaseHelper(this)
        val preferencesHelper = PreferencesHelper(applicationContext)




        val quizOverview = findViewById<Button>(R.id.buttonCreateQuizButton)
        quizOverview.setOnClickListener {
            val name = findViewById<TextView>(R.id.editTextQuizNames).text.toString()
            val description = findViewById<TextView>(R.id.editTextQuizDescription).text.toString()
            val quizID = dbHelper.insertQuiz(name, description).toInt()
            preferencesHelper.setCurrentQuizId(quizID)
            val intent = Intent(this, QuizOverviewActivity::class.java)
            startActivity(intent)
        }

        popNav()
    }

    private fun popNav(){
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