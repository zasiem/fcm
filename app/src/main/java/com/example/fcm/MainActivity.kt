package com.example.fcm

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.example.fcm.Model.Notification
import com.example.fcm.Model.NotificationBody
import com.example.fcm.Model.User
import com.example.fcm.Service.RetrofitFactory
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Callback
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    val TAG = "Main";
    var token = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            var message = et_message.text.toString();
            sendMessage("New Message", message);
        }

        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Log.d("spinner", "non selected");
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedItem: String = p0?.getItemAtPosition(p2).toString();
                setTokenFCM(selectedItem);
            }

        }

        FirebaseApp.initializeApp(this)

        FirebaseDatabase.getInstance().reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d("firebase", "Cancelled snapshot")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val listUser: ArrayList<String> = ArrayList()

                for (data in snapshot.getChildren()) {
                    val user =
                        data.getValue(User::class.java)!!
                    listUser.add(user.token);
                }

                val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    applicationContext,
                    android.R.layout.simple_spinner_dropdown_item,
                    listUser
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                spinner.adapter = adapter
            }
        })

        FirebaseMessaging.getInstance().token.addOnCompleteListener OnCompleteListener@{ task ->
            if (!task.isSuccessful) {
                Log.d(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            if (token != null) {
                Log.d(TAG, token)
//                registerToken(token)
                this.token = token;
            } else {
                Log.d(TAG, "null")
            }
        }
    }

    private fun registerToken(token: String) {
        FirebaseDatabase.getInstance().reference.push().setValue(
            User(
                "redmi",
                token
            )
        );
    }

    private fun sendMessage(title: String, body: String) {
        val retrofit = RetrofitFactory.makeRetrofitService(RetrofitFactory.BASE_URL)
        var call = retrofit.sendNotification(
            NotificationBody(this.token, Notification(body, title))
        )

        Log.d("response", this.token);

        call.enqueue(object: Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("response", response.raw().toString());
                Toast.makeText(applicationContext, "Message Sent!", Toast.LENGTH_LONG);
                et_message.text.clear();
            }

        })
    }

    private fun setTokenFCM(token: String){
        this.token = token;
    }
}
