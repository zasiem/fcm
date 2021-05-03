package com.example.fcm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.fcm.Model.Notification
import com.example.fcm.Model.NotificationBody
import com.example.fcm.Model.User
import com.example.fcm.Service.RetrofitFactory
import com.example.fcm.Service.RetrofitService
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.RequestBody
import okhttp3.ResponseBody

class MainActivity : AppCompatActivity() {

    val TAG = "Main";
    var token = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var message = et_message.text.toString();
        button.setOnClickListener {
            sendMessage("New Message", message);
        }

        FirebaseApp.initializeApp(this)

        FirebaseMessaging.getInstance().token.addOnCompleteListener OnCompleteListener@{ task ->
            if (!task.isSuccessful) {
                Log.d(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            if (token != null) {
                Log.d(TAG, token)
                registerToken(token)
                this.token = token;
            } else {
                Log.d(TAG, "null")
            }
        }
    }

    private fun registerToken(token: String) {
        FirebaseDatabase.getInstance().reference.push().setValue(
            User(
                "Encyn",
                token
            )
        );
    }

    private fun sendMessage(title: String, body: String){
        val retrofit = RetrofitFactory.makeRetrofitService(RetrofitFactory.BASE_URL)
        var callback = retrofit.sendNotification(
            NotificationBody(this.token, Notification(body, title))
        )

        Log.d("callback", callback.toString());
    }
}
