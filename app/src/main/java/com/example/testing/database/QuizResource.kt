package com.example.testing.database

data class QuizResource(
    val quizId: Int,
    val quizName: String,
    val questionText: String,
    val correctAnswer: String,
    val questionNumber: Int,
    val pictureReference: String,
    val wrongAnswer1Text: String,
    val wrongAnswer2Text: String,
    val wrongAnswer3Text: String,
    val multiChoice: Boolean,
    val pictureUsed: Boolean
)
