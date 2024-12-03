package com.example.testing

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testing.database.DatabaseHelper
import com.example.testing.session.PreferencesHelper

class ProfileActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var preferencesHelper: PreferencesHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)

        dbHelper = DatabaseHelper(this)
        val preferencesHelper = PreferencesHelper(applicationContext)

        val user = dbHelper.fetchUser(preferencesHelper.getUserId())



        //Name
        val textViewName = findViewById<TextView>(R.id.textViewName2)
        textViewName.text = user.name
        //Surname
        val textViewSurname = findViewById<TextView>(R.id.textViewSurname2)
        textViewSurname.text = user.surname
        //Username
        val textViewUsername = findViewById<TextView>(R.id.textViewUsername2)
        textViewUsername.text = user.username
        //Password
        val textViewPassword = findViewById<TextView>(R.id.textViewPassword2)
        textViewPassword.text = user.password
        //Email
        //val textViewEmail = findViewById<TextView>(R.id.textViewEmail2)
        //textViewEmail.text = "Lill@gmail.com"
        //Age
        val textViewAge = findViewById<TextView>(R.id.textViewAge2)
        textViewAge.text = user.birthDate

        //Coupons
        val coupons = dbHelper.fetchCouponsByUserID(preferencesHelper.getUserId())
        val containerCop = findViewById<LinearLayout>(R.id.linearLayoutBodyCouponList)


        for(coupon in coupons) {
            val couponListLayout = LayoutInflater.from(this).inflate(R.layout.coupon_list, containerCop, false)
            val textViewCopName = couponListLayout.findViewById<TextView>(R.id.textViewCopName)
            textViewCopName.text = coupon.couponName

            val textViewCopExp = couponListLayout.findViewById<TextView>(R.id.textViewAgeExpire)
            textViewCopExp.text = coupon.couponExpiry

            val textViewCopDetails = couponListLayout.findViewById<TextView>(R.id.textViewDetails)
            textViewCopDetails.text = coupon.couponDetail

            containerCop.addView(couponListLayout)
        }

        val changeName = findViewById<TextView>(R.id.textViewNameChange)
        changeName.setOnClickListener {
            val updated = showChangeDetails(this, dbHelper, changeName, preferencesHelper)
            preferencesHelper.setName(updated)

        }
        val changeSurname = findViewById<TextView>(R.id.textViewSurnameChange)
        changeSurname.setOnClickListener {
            val updated = showChangeDetails(this, dbHelper, changeSurname, preferencesHelper)
            preferencesHelper.setSurname(updated)

        }
        val changeUsername = findViewById<TextView>(R.id.textViewUsernameChange)
        changeUsername.setOnClickListener {
            val updated = showChangeDetails(this, dbHelper, changeUsername, preferencesHelper)
            preferencesHelper.setUsername(updated)

        }
        val changePassword = findViewById<TextView>(R.id.textViewPasswordChange)
        changePassword.setOnClickListener {
            val updated = showChangeDetails(this, dbHelper, changePassword, preferencesHelper)
            preferencesHelper.setPassword(updated)

        }
        val changeBirth = findViewById<TextView>(R.id.textViewAgeChange)
        changeBirth.setOnClickListener {
            val updated = showChangeDetails(this, dbHelper, changeBirth, preferencesHelper)
            preferencesHelper.setBirthDate(updated)

        }
        val changePrefName = findViewById<TextView>(R.id.textViewPrefNameChange)
        changePrefName.setOnClickListener {
            val updated = showChangeDetails(this, dbHelper, changePrefName, preferencesHelper)
            preferencesHelper.setPreferredName(updated)
        }




        //Preferences
        //Preferred name
        val textViewPrefName = findViewById<TextView>(R.id.textViewPrefName2)
        textViewPrefName.text = user.prefName

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

    private fun showChangeDetails(context: Context, dbHelper: DatabaseHelper, textView: TextView, preferencesHelper: PreferencesHelper):String {
        //find info that we are trying to change
        val detailChanging = getStringForViewId(textView.id)

        // Create a dialog builder
        val builder = AlertDialog.Builder(context)
        builder.setTitle(detailChanging)

        // Inflate a custom layout with EditTexts for name and description
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_change_preferences, null)
        builder.setView(dialogView)

        // Get references to the EditText fields
        val detailInput = dialogView.findViewById<EditText>(R.id.editTextDetail)

        // Set up the dialog buttons
        builder.setPositiveButton("Add") { dialog, _ ->
            val detail = detailInput.text.toString()

            if (detail.isNotEmpty()) {
                // Insert the data into the database
                val userField =
                    when(detailChanging){
                        "Change Name" -> "name"
                        "Change Surname" -> "surname"
                        "Change Username" -> "username"
                        "Change Password" -> "password"
                        "Change Birthdate" -> "birth_date"
                        "Change Preferred Name" -> "preferred_name"
                        else -> "Unknown View"
                    }

                dbHelper.updateUserField(preferencesHelper.getUserId(), userField, detail)

                Toast.makeText(context, "Details Updated ", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(context, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss() // Close the dialog without doing anything
        }

        // Show the dialog
        builder.create().show()

        return detailInput.text.toString()
    }

    private fun getStringForViewId(viewId: Int): String {
        return when (viewId) {
            R.id.textViewNameChange -> "Change Name"
            R.id.textViewSurnameChange -> "Change Surname"
            R.id.textViewUsernameChange -> "Change Username"
            R.id.textViewPasswordChange -> "Change Password"
            R.id.textViewAgeChange -> "Change Birthdate"
            R.id.textViewPrefNameChange -> "Change Preferred Name"
            else -> "Unknown View"
        }
    }
}
