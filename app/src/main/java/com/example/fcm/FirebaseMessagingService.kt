package com.example.fcm

import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingService: FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

        Log.d("Token", p0);
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        showNotification(p0.notification?.title.toString(), p0.notification?.body.toString())
    }

    private fun showNotification(title: String, message: String){
        val builder = NotificationCompat.Builder(this, "test-notif")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_background);

        val manager = NotificationManagerCompat.from(this)

        manager.notify(0, builder.build())
    }
}