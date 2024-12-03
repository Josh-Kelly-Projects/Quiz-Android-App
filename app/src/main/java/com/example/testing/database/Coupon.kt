package com.example.testing.database

data class Coupon(
    val couponID: Int,
    val userID: Int,
    val couponName: String,
    val couponDetail: String,
    val couponExpiry: String
)
