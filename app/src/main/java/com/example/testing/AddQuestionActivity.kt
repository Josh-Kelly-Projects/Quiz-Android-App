package com.example.testing

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.testing.database.DatabaseHelper
import com.example.testing.session.PreferencesHelper


class AddQuestionActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.questions_form)

        dbHelper = DatabaseHelper(this) //Database connection
        val preferencesHelper = PreferencesHelper(applicationContext) //Session tracker

        val quizOverview = findViewById<Button>(R.id.buttonCreateQuestion)
        quizOverview.setOnClickListener {
            val question = findViewById<TextView>(R.id.editTextQuizQuestion).text.toString()
            val answer = findViewById<TextView>(R.id.editTextQuizAnswer).text.toString()
            val wrongAnswers = listOf(
                findViewById<TextView>(R.id.editTextQuizAnswer2).text.toString(),
                findViewById<TextView>(R.id.editTextQuizAnswer3).text.toString(),
                findViewById<TextView>(R.id.editTextQuizAnswer4).text.toString()
            )

            val questionID = dbHelper.insertQuestion(question, answer).toInt()
            val wrongAnswersID: MutableList<Int> = mutableListOf()

            for(i in wrongAnswers){
                if(i!="") {
                    val wrongID = dbHelper.getWrongAnswerId(i)
                    if(wrongID == -1) {
                        wrongAnswersID.add(dbHelper.insertWrongAnswers(i).toInt())
                    }else{
                        wrongAnswersID.add(wrongID)
                    }

                }else{
                    wrongAnswersID.add(-1)
                }
            }

            val quizID = preferencesHelper.getCurrentQuizId()
            dbHelper.insertQuizQuestions(quizID, questionID, -1, wrongAnswersID[0],wrongAnswersID[1], wrongAnswersID[2],(dbHelper.getQuestionCount(quizID)+1))
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