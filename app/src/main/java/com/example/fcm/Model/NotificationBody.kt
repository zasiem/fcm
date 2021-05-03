package com.example.fcm.Model

import com.google.gson.annotations.SerializedName

data class NotificationBody(
    @SerializedName("to") val key : String,
    @SerializedName("notification") val body : Notification
) {
    constructor() : this("", Notification("", ""))
}