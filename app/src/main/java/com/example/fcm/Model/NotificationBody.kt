package com.example.fcm.Model

data class NotificationBody(
    val key : String,
    val body : Notification
) {
    constructor() : this("", Notification("", ""))
}