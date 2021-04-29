package com.example.fcm.Model

data class User(
    val name: String,
    val token: String
) {
    constructor() : this("", "");
}