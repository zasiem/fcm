package com.example.fcm.Service

import com.example.fcm.Model.NotificationBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RetrofitService {

    @Headers(
        "Content-Type:application/json",
        "Authorization: Bearer AAAAgoct434:APA91bEse4ETfXROjIHvtI76-xtILLmwTCOFYMNSeg1r6kYy6w5hTEA-jYEgZ5MuAyWDFHtz7NUB3rYaecT0aUAfYVTcfhzrphFaUjJVJoLDI4z4GpL6N0DPO-5hsGuMyq3fy-IIBrJF"
    )
    @POST("fcm/send")
    fun sendNotification(
        @Body data: NotificationBody
    ): Call<ResponseBody>

}