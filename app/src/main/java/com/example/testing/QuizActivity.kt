package com.example.testing

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.testing.database.DatabaseHelper
import com.example.testing.database.QuizResource
import com.example.testing.session.PreferencesHelper

class QuizActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private var currentIndex = 0
    private var answerSelected = ""
    private var answerButtons: List<TextView> = listOf()
    private var isTypedAns = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz)

        val preferencesHelper = PreferencesHelper(applicationContext)

        dbHelper = DatabaseHelper(this)

        val quizResource = dbHelper.getQuizResources(preferencesHelper.getCurrentQuizId())
        val answersList = mutableListOf<String>()


        popQuiz(quizResource[currentIndex])
        currentIndex++

        val submitButton = findViewById<Button>(R.id.buttonSubmit)
        submitButton.setOnClickListener {
            if (currentIndex < quizResource.size) {
                if (isTypedAns) {
                    answerSelected = findViewById<TextView>(R.id.editTextType).text.toString()

                    if (answerSelected == "") {
                        Toast.makeText(this, "enter Answer", Toast.LENGTH_SHORT).show()
                    } else {
                        answersList.add(answerSelected)
                        Toast.makeText(this, "Answer is " + answerSelected, Toast.LENGTH_SHORT).show()

                        val containerPic = findViewById<LinearLayout>(R.id.linearLayoutInside)
                        val containerMultiple = findViewById<LinearLayout>(R.id.linearLayoutMultipleChoice)
                        containerPic.removeAllViews() // Clears the picture container
                        containerMultiple.removeAllViews()

                        val quiz = quizResource[currentIndex]
                        popQuiz(quiz)
                        currentIndex++ // Increment the index for the next button press
                        answerSelected = ""
                        isTypedAns = false
                    }
                } else {
                    if (answerSelected == "") {
                        Toast.makeText(this, "click an answer", Toast.LENGTH_SHORT).show()
                    } else {
                        answersList.add(answerSelected)
                        Toast.makeText(this, "answer clicked is " + answerSelected, Toast.LENGTH_SHORT).show()

                        val containerPic = findViewById<LinearLayout>(R.id.linearLayoutInside)
                        val containerMultiple = findViewById<LinearLayout>(R.id.linearLayoutMultipleChoice)
                        containerPic.removeAllViews() // Clears the picture container
                        containerMultiple.removeAllViews()

                        val quiz = quizResource[currentIndex]
                        popQuiz(quiz)
                        currentIndex++ // Increment the index for the next button press
                        answerSelected = ""
                    }
                }
            } else {
                //items have all been looped through
                Toast.makeText(this, "quiz is finished well done" + answersList.toString(), Toast.LENGTH_LONG).show()
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
            }
        }


        popNav()
    }

    private fun popQuiz(quiz: QuizResource) {
        //Name of quiz
        val textViewName = findViewById<TextView>(R.id.nameTextView)
        textViewName.text = quiz.quizName + " Question: " + quiz.questionNumber


        //Time remaining
        val textViewTime = findViewById<TextView>(R.id.textViewTime)
        textViewTime.text = "Time remaining: 50s"


        //question
        val textViewQuestion = findViewById<TextView>(R.id.textViewQuestion)
        textViewQuestion.text = quiz.questionText

        if (quiz.pictureUsed) {
            //Image
            val containerPic = findViewById<LinearLayout>(R.id.linearLayoutInside)
            val picLayout = LayoutInflater.from(this).inflate(R.layout.quiz_picture, containerPic, false)
            containerPic.addView(picLayout)
        }


        if (quiz.multiChoice) {
            //Multiple choice
            val containerMultiple = findViewById<LinearLayout>(R.id.linearLayoutMultipleChoice)
            val multiLayout = LayoutInflater.from(this).inflate(R.layout.quiz_multi, containerMultiple, false)
            val textViewAnswer1 = multiLayout.findViewById<TextView>(R.id.buttonAnswer1)
            textViewAnswer1.text = quiz.correctAnswer
            val textViewAnswer2 = multiLayout.findViewById<TextView>(R.id.buttonAnswer2)
            textViewAnswer2.text = quiz.wrongAnswer1Text
            val textViewAnswer3 = multiLayout.findViewById<TextView>(R.id.buttonAnswer3)
            textViewAnswer3.text = quiz.wrongAnswer2Text
            val textViewAnswer4 = multiLayout.findViewById<TextView>(R.id.buttonAnswer4)
            textViewAnswer4.text = quiz.wrongAnswer3Text
            containerMultiple.addView(multiLayout)

            textViewAnswer1.setOnClickListener {
                setAnswer(textViewAnswer1)
            }

            textViewAnswer2.setOnClickListener {
                setAnswer(textViewAnswer2)
            }

            textViewAnswer3.setOnClickListener {
                setAnswer(textViewAnswer3)
            }

            textViewAnswer4.setOnClickListener {
                setAnswer(textViewAnswer4)
            }

        } else {
            //Typed answer
            val containerTypes = findViewById<LinearLayout>(R.id.linearLayoutMultipleChoice)
            val typeLayout = LayoutInflater.from(this).inflate(R.layout.quiz_type, containerTypes, false)
            val textViewAnswer = typeLayout.findViewById<TextView>(R.id.editTextType)
            isTypedAns = true
            textViewAnswer.hint = "Type Answer"
            containerTypes.addView(typeLayout)
        }
    }

    private fun setAnswer(selectedButton: TextView) {
        //reset all the buttons
        for (button in answerButtons) {
            button.setBackgroundColor(Color.TRANSPARENT)
        }
        answerSelected = selectedButton.text.toString()
        selectedButton.setBackgroundColor(Color.GREEN)
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
}

