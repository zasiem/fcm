package com.example.fcm.Model

data class Notification(
    val body: String,
    val title: String
) {
    constructor() : this("", "");
}