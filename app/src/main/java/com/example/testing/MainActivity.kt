package com.example.testing

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testing.database.DatabaseHelper
import com.example.testing.session.PreferencesHelper


class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var preferencesHelper: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.main)

        //initialize the db helper and preference helper for context
        dbHelper = DatabaseHelper(this)
        preferencesHelper = PreferencesHelper(this)

        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val isFirstRun = sharedPreferences.getBoolean("is_first_run", true)

        if (isFirstRun) {
            Log.d("Main data insertion", "we're running the data initialization dw we got you")
            //these are for example users and will be for the showcase of the application
            dbHelper.insertUser("Jane", "Doe", "Janie", "987-654-3210", "jane.doe@example.com", "mypassword123", "1992-02-02", true, true)
            dbHelper.insertUser("John", "Doe", "JohnnyBoy", "987-654-3210", "john@ex.com", "mypassword123", "1992-02-02", true, false)

            dbHelper.insertCoupon(1, "Free Quizzicles", "Free fizzie drink", "2025-02-02")
            dbHelper.insertCoupon(1, "Free Burger", "Any burger for free", "2024-12-02")

            dbHelper.insertMenu("Cheese burger", "Bread, lettuce, beef, cheese, sauce, tomato", 30, "cheeseburger", "food")
            dbHelper.insertMenu("Sprite", "", 10, "sprite", "drink")
            dbHelper.insertMenu("Chicken wrap", "Chicken, lettuce, onion, sauce", 55, "chickenwrap", "food")

            dbHelper.insertQuiz("General Knowledge 101", "A quiz to test your general knowledge with 10 interesting questions.")

            val questionTexts = listOf(
                "What is the capital of France?",
                "Who wrote 'Hamlet'?",
                "What is the largest planet in our solar system?",
                "Who painted the Mona Lisa?",
                "What is the boiling point of water?",
                "Who was the first president of the United States?",
                "What is the chemical symbol for gold?",
                "Which ocean is the largest?",
                "What language has the most native speakers?",
                "What year did World War II end?"
            )

            val answerTexts = listOf(
                "Paris",
                "William Shakespeare",
                "Jupiter",
                "Leonardo da Vinci",
                "100°C",
                "George Washington",
                "Au",
                "Pacific Ocean",
                "Mandarin",
                "1945"
            )

            for (i in questionTexts.indices) {
                dbHelper.insertQuestion(questionTexts[i], answerTexts[i])
            }

            val wrongAnswers = listOf(
                "Rome",
                "Berlin",
                "Madrid",
                "Charles Dickens",
                "Mark Twain",
                "Jane Austen",
                "Vincent van Gogh",
                "Michelangelo",
                "Donatello",
                "Abraham Lincoln",
                "Barak Obama",
                "George Bush",
                "Ag",
                "Gd",
                "Te",
                "English",
                "Hindi",
                "Tamil"
            )
            for (i in wrongAnswers.indices) {
                dbHelper.insertWrongAnswers(wrongAnswers[i])
            }

            dbHelper.insertPicture("placeholder.jpg", "this is a place holder")


            dbHelper.insertQuizQuestions(1, 1, -1, 1, 2, 3, 1)
            dbHelper.insertQuizQuestions(1, 2, -1, 4, 5, 6, 2)
            dbHelper.insertQuizQuestions(1, 3, -1, -1, -1, -1, 3)
            dbHelper.insertQuizQuestions(1, 4, -1, 7, 8, 9, 4)
            dbHelper.insertQuizQuestions(1, 5, 1, -1, -1, -1, 5)
            dbHelper.insertQuizQuestions(1, 6, -1, 10, 11, 12, 6)
            dbHelper.insertQuizQuestions(1, 7, -1, 13, 14, 15, 7)
            dbHelper.insertQuizQuestions(1, 8, -1, -1, -1, -1, 8)
            dbHelper.insertQuizQuestions(1, 9, -1, 16, 17, 18, 9)
            dbHelper.insertQuizQuestions(1, 10, 1, -1, -1, -1, 10)


            sharedPreferences.edit().putBoolean("is_first_run", false).apply()
        }




        findViewById<Button>(R.id.buttonLogin).setOnClickListener {
            val username = findViewById<EditText>(R.id.editTextUsername).text.toString().trim()
            val password = findViewById<EditText>(R.id.editTextPassword).text.toString().trim()

            if (attemptLogin(username, password)) {
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, username + password + "Login failed: Invalid credentials", Toast.LENGTH_SHORT).show()
            }

        }

        val registerButton = findViewById<Button>(R.id.buttonRegister)
        registerButton.setOnClickListener {
            // Navigate to the NewActivity (replace NewActivity with the actual class name)
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val loginAsGuestButton = findViewById<Button>(R.id.buttonLoginAsGuest)
        loginAsGuestButton.setOnClickListener {
            // Navigate to the NewActivity (replace NewActivity with the actual class name)
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun attemptLogin(username: String, password: String): Boolean {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        var loginSuccessful = false
        var isAdmin = false
        var userId = -1

        // Query the users table to check for matching username and password
        val cursor: Cursor = db.query(
            "users",
            arrayOf(
                "id",
                "isAdmin",
                "name",
                "surname",
                "preferred_name",
                "phone_number",
                "username",
                "password",
                "birth_date",
                "lightmode"
            ), // Selecting the id field (you can add more fields if needed)
            "username = ? AND password = ?", // WHERE clause
            arrayOf(username, password), // Arguments for WHERE clause
            null, // Group by
            null, // Having
            null // Order by
        )

        // Check if a matching user was found
        if (cursor.moveToFirst()) {
            loginSuccessful = true
            preferencesHelper.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow("id")))
            preferencesHelper.setIsAdmin(cursor.getInt(cursor.getColumnIndexOrThrow("isAdmin")) == 1)
            preferencesHelper.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")))
            preferencesHelper.setSurname(cursor.getString(cursor.getColumnIndexOrThrow("surname")))
            preferencesHelper.setPreferredName(cursor.getString(cursor.getColumnIndexOrThrow("preferred_name")))
            preferencesHelper.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow("phone_number")))
            preferencesHelper.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("username")))
            preferencesHelper.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password"))) // Consider security best practices for storing passwords
            preferencesHelper.setBirthDate(cursor.getString(cursor.getColumnIndexOrThrow("birth_date")))
            preferencesHelper.setLightMode(cursor.getInt(cursor.getColumnIndexOrThrow("lightmode")) == 1)
            preferencesHelper.setLoggedIn(true)
        }

        cursor.close() // Close the cursor
        db.close() // Close the database

        return loginSuccessful
    }

}
