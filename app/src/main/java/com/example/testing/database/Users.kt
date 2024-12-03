package com.example.testing.database

import java.sql.Date

data class Users(
    val name: String,
    val surname: String,
    val username: String,
    val password: String,
    val birthDate: String,
    val prefName: String
)
