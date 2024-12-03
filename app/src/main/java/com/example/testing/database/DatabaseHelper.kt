package com.example.testing.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "quiz.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {

        //Create user's table
        db.execSQL(
            """
            CREATE TABLE users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                surname TEXT NOT NULL,
                preferred_name TEXT,
                phone_number TEXT,
                username TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL,
                birth_date TEXT,
                lightmode BOOLEAN NOT NULL CHECK (lightmode IN (0, 1)),
                isAdmin BOOLEAN NOT NULL CHECK (isAdmin IN (0, 1)) DEFAULT 0
            )
        """
        )

        //Create quizzes table
        db.execSQL(
            """
            CREATE TABLE quizzes (
                quiz_id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                description TEXT
            )
        """
        )

        //Create pictures table
        db.execSQL(
            """
            CREATE TABLE pictures (
                picture_id INTEGER PRIMARY KEY AUTOINCREMENT,
                picture_reference TEXT NOT NULL,
                description TEXT
            )
        """
        )

        //Create questions table
        db.execSQL(
            """
            CREATE TABLE questions (
                question_id INTEGER PRIMARY KEY AUTOINCREMENT,
                question TEXT NOT NULL,
                answer TEXT NOT NULL
            )
        """
        )

        //Create wrong_answers table
        db.execSQL(
            """
            CREATE TABLE wrong_answers (
                wrong_answer_id INTEGER PRIMARY KEY AUTOINCREMENT,
                wrong_answer TEXT NOT NULL
            )
        """
        )

        //Create quiz_questions table
        db.execSQL(
            """
            CREATE TABLE quiz_questions (
                quiz_question_id INTEGER PRIMARY KEY AUTOINCREMENT,
                quiz_id INTEGER NOT NULL,
                question_id INTEGER NOT NULL,
                picture_id INTEGER,
                wrong_answer_1_id INTEGER,
                wrong_answer_2_id INTEGER,
                wrong_answer_3_id INTEGER,
                question_number INTEGER NOT NULL,
                FOREIGN KEY (quiz_id) REFERENCES quizzes (quiz_id),
                FOREIGN KEY (question_id) REFERENCES questions (question_id),
                FOREIGN KEY (picture_id) REFERENCES pictures (picture_id),
                FOREIGN KEY (wrong_answer_1_id) REFERENCES wrong_answers (wrong_answer_id),
                FOREIGN KEY (wrong_answer_2_id) REFERENCES wrong_answers (wrong_answer_id),
                FOREIGN KEY (wrong_answer_3_id) REFERENCES wrong_answers (wrong_answer_id)
            )
        """
        )

        //Create coupons table
        db.execSQL(
            """
            CREATE TABLE coupons (
                couponID INTEGER PRIMARY KEY AUTOINCREMENT,
                id INTEGER,
                coupon_name TEXT,
                coupon_detail TEXT,
                coupon_expiry TEXT,
                FOREIGN KEY (id) REFERENCES users (id)
            )
        """
        )

        //Create menu item table
        db.execSQL(
            """
            CREATE TABLE menu (
                itemID INTEGER PRIMARY KEY AUTOINCREMENT,
                item_name TEXT,
                item_detail TEXT,
                item_price INTEGER,
                item_picture TEXT,
                item_type TEXT
            )
        """
        )
    }

    //Insert user content
    fun insertUser(
        name: String,
        surname: String,
        preferredName: String?,
        phoneNumber: String,
        username: String,
        password: String,
        birthDate: String,
        lightmode: Boolean,
        isAdmin: Boolean
    ) {
        val db: SQLiteDatabase = writableDatabase
        val values = ContentValues().apply {
            put("name", name)
            put("surname", surname)
            put("preferred_name", preferredName)
            put("phone_number", phoneNumber)
            put("username", username)
            put("password", password)
            put("birth_date", birthDate)
            put("lightmode", if (lightmode) 1 else 0) // Boolean stored as integer
            put("isAdmin", if (isAdmin) 1 else 0)
        }

        val newRowId = db.insert("users", null, values)
        if (newRowId == -1L) {
            Log.e("Database", "Error inserting user")
        } else {
            Log.d("Database", "User added with ID: $newRowId")
        }

        //Close the database connection
        db.close()
    }


    //Pass the information and the row
    fun updateUserField(userId: Int, fieldName: String, newValue: Any) {
        val db: SQLiteDatabase = writableDatabase
        val values = ContentValues().apply {
            when (newValue) {
                is String -> put(fieldName, newValue)
                is Int -> put(fieldName, newValue)
                is Boolean -> put(fieldName, if (newValue) 1 else 0)
                else -> throw IllegalArgumentException("Unsupported data type")
            }
        }

        val rowsAffected = db.update(
            "users",
            values,
            "id = ?",
            arrayOf(userId.toString())
        )

        if (rowsAffected > 0) {
            Log.d("Database", "Updated $rowsAffected row(s)")
        } else {
            Log.e("Database", "No rows updated")
        }

        //Close the database connection
        db.close()
    }

    //Fetch user data
    fun fetchAllUsers() {
        val db: SQLiteDatabase = readableDatabase
        val cursor = db.query(
            "users",
            arrayOf("id", "name", "surname", "username"),
            null, null, null, null, null
        )

        with(cursor) {
            while (moveToNext()) {
                val userId = getInt(getColumnIndexOrThrow("id"))
                val userName = getString(getColumnIndexOrThrow("name"))
                val userSurname = getString(getColumnIndexOrThrow("surname"))
                val userUsername = getString(getColumnIndexOrThrow("username"))
                Log.d(
                    "Database",
                    "User: $userId, Name: $userName $userSurname, Username: $userUsername"
                )
            }
        }
        cursor.close()
        //Close the database connection
        db.close()
    }

    fun fetchUser(userID: Int): Users {
        val db: SQLiteDatabase = readableDatabase
        val user: Users
        val cursor = db.query(
            "users",
            arrayOf("name", "surname", "username", "password", "birth_date", "preferred_name"),
            "id = ?",
            arrayOf(userID.toString()),
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            user = Users(
                name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                surname = cursor.getString(cursor.getColumnIndexOrThrow("surname")),
                username = cursor.getString(cursor.getColumnIndexOrThrow("username")),
                password = cursor.getString(cursor.getColumnIndexOrThrow("password")),
                birthDate = cursor.getString(cursor.getColumnIndexOrThrow("birth_date")),
                prefName = cursor.getString(cursor.getColumnIndexOrThrow("preferred_name"))
            )
            cursor.close()
            db.close()
            return user
        } else {
            cursor.close()
            db.close()
            throw NoSuchElementException("User with ID $userID not found")
        }


    }

    //Insert coupons
    fun insertCoupon(userId: Int, couponName: String, couponDetail: String, couponExpiry: String) {
        val db: SQLiteDatabase = writableDatabase
        val values = ContentValues().apply {
            put("id", userId) // Foreign key linking to the user
            put("coupon_name", couponName)
            put("coupon_detail", couponDetail)
            put("coupon_expiry", couponExpiry)
        }

        val newRowId = db.insert("coupons", null, values)
        if (newRowId == -1L) {
            Log.e("Database", "Error inserting coupon")
        } else {
            Log.d("Database", "Coupon added with ID: $newRowId")
        }

        //Close the database connection
        db.close()
    }

    //Fetch all coupons based on the user's ID
    fun fetchCouponsByUserID(userID: Int): List<Coupon> {
        val db: SQLiteDatabase = readableDatabase
        val coupons = mutableListOf<Coupon>()

        val cursor = db.query(
            "coupons",
            arrayOf("couponID", "id", "coupon_name", "coupon_detail", "coupon_expiry"),
            "id = ?",
            arrayOf(userID.toString()),
            null,
            null,
            null
        )

        while (cursor.moveToNext()) {
            val coupon = Coupon(
                couponID = cursor.getInt(cursor.getColumnIndexOrThrow("couponID")),
                userID = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                couponName = cursor.getString(cursor.getColumnIndexOrThrow("coupon_name")),
                couponDetail = cursor.getString(cursor.getColumnIndexOrThrow("coupon_detail")),
                couponExpiry = cursor.getString(cursor.getColumnIndexOrThrow("coupon_expiry"))
            )
            coupons.add(coupon)
        }

        cursor.close()
        //Close the database connection
        db.close()
        //Return all the coupons for the user
        return coupons
    }

    //Insert into the menu table
    fun insertMenu(
        itemName: String,
        itemDetail: String,
        itemPrice: Int,
        itemPicture: String,
        itemType: String
    ) {
        val db: SQLiteDatabase = writableDatabase
        val values = ContentValues().apply {
            put("item_name", itemName)
            put("item_detail", itemDetail)
            put("item_price", itemPrice)
            put("item_picture", itemPicture)
            put("item_type", itemType)
        }

        val newRowId = db.insert("menu", null, values)
        if (newRowId == -1L) {
            Log.e("Database", "Error inserting coupon")
        } else {
            Log.d("Database", "item added with ID: $newRowId")
        }

        //Close the database connection
        db.close()
    }

    //Fetch the menu items from the database
    fun fetchMenuItems(): List<MenuItem> {
        val db: SQLiteDatabase = readableDatabase
        val menuItems = mutableListOf<MenuItem>()

        val cursor = db.query(
            "menu",
            null,
            null,
            null,
            null,
            null,
            "item_name ASC"
        )

        //Iterate over each row
        with(cursor) {
            while (moveToNext()) {
                val itemID = getInt(getColumnIndexOrThrow("itemID"))
                val itemName = getString(getColumnIndexOrThrow("item_name"))
                val itemDetail = getString(getColumnIndexOrThrow("item_detail"))
                val itemPrice = getInt(getColumnIndexOrThrow("item_price"))
                val itemPicture = getString(getColumnIndexOrThrow("item_picture"))
                val itemType = getString(getColumnIndexOrThrow("item_type"))

                val menuItem =
                    MenuItem(itemID, itemName, itemDetail, itemPrice, itemPicture, itemType)
                menuItems.add(menuItem)
            }
            close()
        }

        //Close the database connection
        db.close()
        //Return the menu items
        return menuItems
    }

    //Insert into the quiz table
    fun insertQuiz(name: String, description: String): Long {
        val db: SQLiteDatabase = writableDatabase
        val values = ContentValues().apply {
            put("name", name)
            put("description", description)
        }

        val newRowId = db.insert("quizzes", null, values)
        if (newRowId == -1L) {
            Log.e("Database", "Error inserting quiz")
        } else {
            Log.d("Database", "quiz added with ID: $newRowId")
        }

        //Close the database connection
        db.close()
        return newRowId
    }

    //Insert pictures into the database
    fun insertPicture(pictureReference: String, description: String): Long {
        val db: SQLiteDatabase = writableDatabase
        val values = ContentValues().apply {
            put("picture_reference", pictureReference)
            put("description", description)
        }

        val newRowId = db.insert("pictures", null, values)
        if (newRowId == -1L) {
            Log.e("Database", "Error inserting pic")
        } else {
            Log.d("Database", "pic added with ID: $newRowId")
        }

        //Close the database connection
        db.close()
        return newRowId
    }

    //Insert questions into the database
    fun insertQuestion(question: String, answer: String):Long {
        val db: SQLiteDatabase = writableDatabase
        val values = ContentValues().apply {
            put("question", question)
            put("answer", answer)
        }

        val newRowId = db.insert("questions", null, values)
        if (newRowId == -1L) {
            Log.e("Database", "Error inserting question")
        } else {
            Log.d("Database", "question added with ID: $newRowId")
        }

        //Close the database connection
        db.close()
        return newRowId
    }

    //Insert the wrong answers into the database
    fun insertWrongAnswers(wrongAnswer: String):Long {
        val db: SQLiteDatabase = writableDatabase
        val values = ContentValues().apply {
            put("wrong_answer", wrongAnswer)
        }

        val newRowId = db.insert("wrong_answers", null, values)
        if (newRowId == -1L) {
            Log.e("Database", "Error inserting question")
        } else {
            Log.d("Database", "question added with ID: $newRowId")
        }

        //Close the database connection
        db.close()
        return newRowId
    }

    fun getWrongAnswerId(wrongAnswerText: String): Int {
        val db: SQLiteDatabase = readableDatabase
        var wrongAnswerId = -1  //Default to -1 if no rows are found

        //Query to find the row with the given wrong answer text
        val cursor = db.query(
            "wrong_answers",
            arrayOf("wrong_answer_id"),
            "wrong_answer = ?",
            arrayOf(wrongAnswerText),
            null,
            null,
            null
        )

        //Check if any rows match the query
        if (cursor.moveToFirst()) {
            wrongAnswerId = cursor.getInt(cursor.getColumnIndexOrThrow("wrong_answer_id"))
        }

        cursor.close()
        db.close()

        return wrongAnswerId
    }

    //Inserts the normalized link table entry
    fun insertQuizQuestions(quizID: Int, questionID: Int, pictureID: Int, wrongAns1ID: Int, wrongAns2ID: Int, wrongAns3ID: Int, questionNumber: Int) {
        val db: SQLiteDatabase = writableDatabase
        val values = ContentValues().apply {
            put("quiz_id", quizID)
            put("question_id", questionID)
            put("picture_id", pictureID)
            put("wrong_answer_1_id", wrongAns1ID)
            put("wrong_answer_2_id", wrongAns2ID)
            put("wrong_answer_3_id", wrongAns3ID)
            put("question_number", questionNumber)
        }

        val newRowId = db.insert("quiz_questions", null, values)
        if (newRowId == -1L) {
            Log.e("Database", "Error inserting user")
        } else {
            Log.d("Database", "User added with ID: $newRowId")
        }

        db.close()
    }

    fun getQuestionCount(quizID: Int): Int {
        val db: SQLiteDatabase = readableDatabase
        var questionCount = 0

        //Query to count the number of questions for the given quiz ID
        val cursor = db.rawQuery(
            "SELECT COUNT(*) FROM quiz_questions WHERE quiz_id = ?",
            arrayOf(quizID.toString())
        )

        if (cursor.moveToFirst()) {
            questionCount = cursor.getInt(0) //Get the count from the first column
        }

        cursor.close()
        db.close()

        return questionCount
    }

    //Gets all the information for a quiz
    fun getQuizResources(quizID: Int): List<QuizResource> {
        val db: SQLiteDatabase = readableDatabase
        val resources = mutableListOf<QuizResource>()

        val cursor = db.query(
            "quiz_questions",
            null,
            "quiz_id = ?",
            arrayOf(quizID.toString()),
            null,
            null,
            "question_number ASC"
        )

        //Iterate over each row in the result and put into the resources object
        with(cursor) {
            while (moveToNext()) {

                var quizName = ""
                var questionText = ""
                var correctAnswer = ""
                var pictureReference = ""
                var wrongAnswer1Text = ""
                var wrongAnswer2Text = ""
                var wrongAnswer3Text = ""
                var multiChoice = false
                var pictureUsed = false

                val quizQuestionID = getInt(getColumnIndexOrThrow("quiz_question_id"))
                val queriedQuizID = getInt(getColumnIndexOrThrow("quiz_id"))
                val questionID = getInt(getColumnIndexOrThrow("question_id"))
                val pictureID = getInt(getColumnIndexOrThrow("picture_id"))
                val wrong1 = getInt(getColumnIndexOrThrow("wrong_answer_1_id"))
                val wrong2 = getInt(getColumnIndexOrThrow("wrong_answer_2_id"))
                val wrong3 = getInt(getColumnIndexOrThrow("wrong_answer_3_id"))
                val questionNumber = getInt(getColumnIndexOrThrow("question_number"))

                val quizCursor = db.query(
                    "quizzes",
                    arrayOf("name"),
                    "quiz_id = ?",
                    arrayOf(queriedQuizID.toString()),
                    null,
                    null,
                    null
                )
                with(quizCursor) {
                    while (moveToNext()) {
                        quizName = getString(getColumnIndexOrThrow("name"))
                    }
                }
                quizCursor.close()

                //Gets the questions
                val questionCursor = db.query(
                    "questions",
                    arrayOf("question", "answer"),
                    "question_id = ?",
                    arrayOf(questionID.toString()),
                    null,
                    null,
                    null
                )
                with(questionCursor) {
                    while (moveToNext()) {
                        questionText = getString(getColumnIndexOrThrow("question"))
                        correctAnswer = getString(getColumnIndexOrThrow("answer"))
                    }
                }
                questionCursor.close()

                //Getting the picture reference
                if (pictureID != -1) {
                    val picCursor = db.query(
                        "pictures",
                        arrayOf("picture_reference"),
                        "picture_id = ?",
                        arrayOf(pictureID.toString()),
                        null,
                        null,
                        null
                    )
                    //Iterate over the picCursor
                    with(picCursor) {
                        while (moveToNext()) {
                            pictureReference = getString(getColumnIndexOrThrow("picture_reference"))
                        }
                    }
                    pictureUsed = true
                    picCursor.close()
                }
                //Pic done

                //Getting the wrong options
                if (wrong1 != -1) {
                    val wrongIDList: MutableList<Int> = mutableListOf()
                    wrongIDList.add(0, wrong1)
                    wrongIDList.add(1, wrong2)
                    wrongIDList.add(2, wrong3)

                    val wrongTextList: MutableList<String> = mutableListOf()

                    for (i in 0 until 3) {
                        val wrongCursor = db.query(
                            "wrong_answers",
                            arrayOf("wrong_answer"),
                            "wrong_answer_id = ?",
                            arrayOf(wrongIDList[i].toString()),
                            null,
                            null,
                            null
                        )
                        //Iterate over the picCursor
                        with(wrongCursor) {
                            while (moveToNext()) {
                                wrongTextList.add(
                                    i,
                                    getString(getColumnIndexOrThrow("wrong_answer"))
                                )
                            }
                        }
                        wrongCursor.close()
                    }
                    wrongAnswer1Text = wrongTextList[0]
                    wrongAnswer2Text = wrongTextList[1]
                    wrongAnswer3Text = wrongTextList[2]
                    multiChoice = true
                }

                //Add the resource that is collected
                resources.add(
                    QuizResource(
                        quizId = queriedQuizID,
                        quizName = quizName,
                        questionText = questionText,
                        correctAnswer = correctAnswer,
                        pictureReference = pictureReference,
                        questionNumber = questionNumber,
                        wrongAnswer1Text = wrongAnswer1Text,
                        wrongAnswer2Text = wrongAnswer2Text,
                        wrongAnswer3Text = wrongAnswer3Text,
                        multiChoice = multiChoice,
                        pictureUsed = pictureUsed
                    )
                )
            }
        }
        cursor.close()
        db.close()
        return resources
    }

    //Gets all the quizzes
    fun fetchQuizzes(): List<Quiz> {
        val db: SQLiteDatabase = readableDatabase
        val quizzes = mutableListOf<Quiz>()

        //Query to select all quizzes
        val cursor = db.query(
            "quizzes",
            arrayOf("quiz_id", "name", "description"),
            null,
            null,
            null,
            null,
            null
        )

        //Loop through the result set and map each row to a Quiz object
        if (cursor.moveToFirst()) {
            do {
                val quizId = cursor.getInt(cursor.getColumnIndexOrThrow("quiz_id"))
                val quizName = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val quizDescription = cursor.getString(cursor.getColumnIndexOrThrow("description"))

                val quiz = Quiz(quizId, quizName, quizDescription)
                quizzes.add(quiz)
            } while (cursor.moveToNext())
        }
        cursor.close()

        return quizzes
    }

    //Fetches specific quizzes
    fun fetchQuizById(quizId: Int): Quiz? {
        val db: SQLiteDatabase = readableDatabase
        var quiz: Quiz? = null

        //Query to select the quiz with the specified ID
        val cursor = db.query(
            "quizzes",
            arrayOf("quiz_id", "name", "description"),
            "quiz_id = ?",
            arrayOf(quizId.toString()),
            null,
            null,
            null
        )

        //If the cursor is not empty, get the quiz data
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("quiz_id"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))

            quiz = Quiz(id, name, description)
        }
        cursor.close()

        return quiz
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //Drop old tables if they exist
        db.execSQL("DROP TABLE IF EXISTS quiz_questions")
        db.execSQL("DROP TABLE IF EXISTS quizzes")
        db.execSQL("DROP TABLE IF EXISTS pictures")
        db.execSQL("DROP TABLE IF EXISTS questions")
        db.execSQL("DROP TABLE IF EXISTS wrong_answers")
        onCreate(db)
    }
}