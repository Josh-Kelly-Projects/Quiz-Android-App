package com.example.testing

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.testing.database.DatabaseHelper
import com.example.testing.database.QuizResource
import com.example.testing.session.PreferencesHelper

class QuizOverviewActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var preferencesHelper: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz_overveiw)

        //For querying the db and storing the quiz ID
        dbHelper = DatabaseHelper(this)
        val preferencesHelper = PreferencesHelper(applicationContext)

        //Questions list
        val containerQuestionOverview = findViewById<LinearLayout>(R.id.linearLayoutInsideDynamic)

        val questions = dbHelper.getQuizResources(preferencesHelper.getCurrentQuizId())

        val textViewName = findViewById<TextView>(R.id.textViewQizName)
        textViewName.text = dbHelper.fetchQuizById(preferencesHelper.getCurrentQuizId())!!.quizName


        for (i in questions) {
            val questionListLayout = LayoutInflater.from(this).inflate(R.layout.question_listings, containerQuestionOverview, false)

            val textViewIDNum = questionListLayout.findViewById<TextView>(R.id.textViewID)
            textViewIDNum.text = i.questionNumber.toString()

            val textViewQuestions = questionListLayout.findViewById<TextView>(R.id.textViewQuestQuestion)
            textViewQuestions.text = i.questionText

            val textViewAnswers = questionListLayout.findViewById<TextView>(R.id.textViewAnsAnswer)
            textViewAnswers.text = i.correctAnswer
            if (i.multiChoice) {
                val textViewInAnswer1 = questionListLayout.findViewById<TextView>(R.id.textViewAnsAnswer2)
                textViewInAnswer1.text = i.wrongAnswer1Text

                val textViewInAnswer2 = questionListLayout.findViewById<TextView>(R.id.textViewAnsAnswer3)
                textViewInAnswer2.text = i.wrongAnswer2Text

                val textViewInAnswer3 = questionListLayout.findViewById<TextView>(R.id.textViewAnsAnswer4)
                textViewInAnswer3.text = i.wrongAnswer3Text

            }else{
                questionListLayout.findViewById<TextView>(R.id.textViewAns2).text = "Answer Is Typed"
                val textViewInAnswer1 = questionListLayout.findViewById<TextView>(R.id.textViewAnsAnswer2)
                textViewInAnswer1.visibility = View.GONE

                questionListLayout.findViewById<TextView>(R.id.textViewAns3).visibility = View.GONE
                val textViewInAnswer2 = questionListLayout.findViewById<TextView>(R.id.textViewAnsAnswer3)
                textViewInAnswer2.visibility = View.GONE

                questionListLayout.findViewById<TextView>(R.id.textViewAns4).visibility = View.GONE
                val textViewInAnswer3 = questionListLayout.findViewById<TextView>(R.id.textViewAnsAnswer4)
                textViewInAnswer3.visibility = View.GONE
            }

            val textViewLength = questionListLayout.findViewById<TextView>(R.id.textViewLenLength)
            textViewLength.text = "50s"

            containerQuestionOverview.addView(questionListLayout)
        }

        val addQuestions = findViewById<Button>(R.id.buttonAdd)
        addQuestions.setOnClickListener {
            val intent = Intent(this, AddQuestionActivity::class.java)
            startActivity(intent)
        }
        popNav()

    }

    private fun popNav() {
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

    override fun onBackPressed(){
        val preferencesHelper = PreferencesHelper(applicationContext)
        preferencesHelper.clearCurrentQuizId()
        super.onBackPressed()
    }
}