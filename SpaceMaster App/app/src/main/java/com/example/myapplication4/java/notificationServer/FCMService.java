package com.example.myapplication4.java.notificationServer;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface FCMService {
    @Headers({
            "Content-Type: application/json",
            "Authorization: key=AAAAzG2Vi80:APA91bErVl5ZPoLn02V7NH_H_aOyA1ncmKzDBzFTmA22aao2DHTwE7Cqe7nGYgdbcLkE5wHGHRw_djdCdQFhFtxftLTTxP4MKpJxWerNqLZw4tzQbSMd8h6mUwmBJoc02laMRn4VlLBK"
    })
    @POST("fcm/send")
    Call<FCMResponse> sendNotification(@Body FCMRequest body);
}

