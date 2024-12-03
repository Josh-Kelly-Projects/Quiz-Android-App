package com.example.testing

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.testing.database.DatabaseHelper
import com.example.testing.session.PreferencesHelper

class CreateQuizActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var preferencesHelper: PreferencesHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_quiz)

        //for querying the db and storing the quiz ID
        dbHelper = DatabaseHelper(this)
        val preferencesHelper = PreferencesHelper(applicationContext)

        val quiz = dbHelper.fetchQuizzes()

        //Quiz list
        val containerQuiz = findViewById<LinearLayout>(R.id.linearLayoutInside)
        for(i in quiz) {
            val quizListLayout = LayoutInflater.from(this).inflate(R.layout.create_quiz_list, containerQuiz, false)

            val quizList = quizListLayout.findViewById<LinearLayout>(R.id.linearLayoutDynamicText)

            val textViewQuizName = quizListLayout.findViewById<TextView>(R.id.textViewQuizName)
            textViewQuizName.text = i.quizName

            val textViewQuizQuestions = quizListLayout.findViewById<TextView>(R.id.textViewQuizID)
            textViewQuizQuestions.text = i.quizId.toString()

            val textViewQuizDetails = quizListLayout.findViewById<TextView>(R.id.textViewQuizDetails)
            textViewQuizDetails.text = i.quizDescription

            val textViewQuizLength = quizListLayout.findViewById<TextView>(R.id.textViewLength)
            textViewQuizLength.text = "Length: 50m"

            containerQuiz.addView(quizListLayout)

            quizList.setOnClickListener {
                val intent = Intent(this, QuizOverviewActivity::class.java)
                preferencesHelper.setCurrentQuizId(textViewQuizQuestions.text.toString().toInt())
                startActivity(intent)
            }
        }

        val createQuizForm = findViewById<Button>(R.id.buttonNewQuiz)
        createQuizForm.setOnClickListener {
            val intent = Intent(this, CreateQuizFormActivity::class.java)
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