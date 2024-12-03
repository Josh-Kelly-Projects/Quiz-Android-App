package com.example.testing.session

import android.content.Context
import android.content.SharedPreferences

class PreferencesHelper(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun setUserId(userId: Int) {
        sharedPreferences.edit().putInt("user_id", userId).apply()
    }

    fun getUserId(): Int {
        return sharedPreferences.getInt("user_id", -1) // Return -1 if not found
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean("is_logged_in", isLoggedIn).apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("is_logged_in", false)
    }

    fun setIsAdmin(isAdmin: Boolean) {
        sharedPreferences.edit().putBoolean("is_admin", isAdmin).apply()
    }

    fun isAdmin(): Boolean {
        return sharedPreferences.getBoolean("is_admin", false) // Default to false if not set
    }

    fun setName(name: String) {
        sharedPreferences.edit().putString("name", name).apply()
    }

    fun getName(): String? {
        return sharedPreferences.getString("name", null) // Return null if not found
    }

    fun setSurname(surname: String) {
        sharedPreferences.edit().putString("surname", surname).apply()
    }

    fun getSurname(): String? {
        return sharedPreferences.getString("surname", null) // Return null if not found
    }

    fun setPreferredName(preferredName: String) {
        sharedPreferences.edit().putString("preferred_name", preferredName).apply()
    }

    fun getPreferredName(): String? {
        return sharedPreferences.getString("preferred_name", null) // Return null if not found
    }

    fun setPhoneNumber(phoneNumber: String) {
        sharedPreferences.edit().putString("phone_number", phoneNumber).apply()
    }

    fun getPhoneNumber(): String? {
        return sharedPreferences.getString("phone_number", null) // Return null if not found
    }

    fun setUsername(username: String) {
        sharedPreferences.edit().putString("username", username).apply()
    }

    fun getUsername(): String? {
        return sharedPreferences.getString("username", null) // Return null if not found
    }

    fun setPassword(password: String) {
        sharedPreferences.edit().putString("password", password).apply()
    }

    fun getPassword(): String? {
        return sharedPreferences.getString("password", null) // Return null if not found
    }

    fun setBirthDate(birthDate: String) {
        sharedPreferences.edit().putString("birth_date", birthDate).apply()
    }

    fun getBirthDate(): String? {
        return sharedPreferences.getString("birth_date", null) // Return null if not found
    }

    fun setLightMode(lightMode: Boolean) {
        sharedPreferences.edit().putBoolean("light_mode", lightMode).apply()
    }

    fun isLightMode(): Boolean {
        return sharedPreferences.getBoolean("light_mode", false) // Default to false if not set
    }

    fun logout() {
        sharedPreferences.edit().clear().apply() // Clear all preferences
    }


    //quiz section to link the quiz creation to the add question by passing the quiz id through a shared preference
    // Method to save the current quiz ID
    fun setCurrentQuizId(quizId: Int) {
        sharedPreferences.edit().putInt("current_quiz_id", quizId).apply()
    }

    // Method to retrieve the current quiz ID
    fun getCurrentQuizId(): Int {
        return sharedPreferences.getInt("current_quiz_id", -1) // Return -1 if not found
    }

    // Method to clear the current quiz ID when done
    fun clearCurrentQuizId() {
        sharedPreferences.edit().remove("current_quiz_id").apply()
    }
}

