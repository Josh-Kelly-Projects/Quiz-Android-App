package com.example.testing

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.testing.database.DatabaseHelper

class RegisterActivity : AppCompatActivity(){
    private lateinit var dbHelper: DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        dbHelper = DatabaseHelper(this)

        val registerButton = findViewById<Button>(R.id.buttonRegister)
        registerButton.setOnClickListener {

            val name = findViewById<EditText>(R.id.editTextName).text.toString().trim()
            val surname = findViewById<EditText>(R.id.editTextSurname).text.toString().trim()
            val email = findViewById<EditText>(R.id.editTextEmail).text.toString().trim()
            val phoneNumber = findViewById<EditText>(R.id.editTextPhoneNumber).text.toString().trim()
            val birthday = findViewById<EditText>(R.id.editTextBirthday).text.toString().trim()
            val prefName = findViewById<EditText>(R.id.editTextPreferred).text.toString().trim()
            val password = findViewById<EditText>(R.id.editTextPassword).text.toString().trim()

            dbHelper.insertUser(name, surname, prefName, phoneNumber, email, password, birthday, true, false)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}