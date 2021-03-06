package com.mbds.geoffreyroman.messagerie;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiService {


    static final String BASE_URL = "http://baobab.tokidev.fr/";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    Call createUser(String params, Callback callback) throws IOException {
        RequestBody body = RequestBody.create(JSON, params);
        Request request = new Request.Builder()
                .url(BASE_URL + "api/createUser")
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    Call login(String params, Callback callback) throws IOException {
        RequestBody body = RequestBody.create(JSON, params);
        Request request = new Request.Builder()
                .url(BASE_URL + "api/login")
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    Call getMessages(String token, Callback callback) throws IOException{
        Request request = new Request.Builder()
                .url(BASE_URL + "api/fetchMessages")
                .addHeader("Authorization","Bearer " + token)
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    Call sendMessage(String token, String message, String receiver, Callback callback) throws IOException, JSONException {
        JSONObject RequestBodyJson = new JSONObject();

        RequestBodyJson.put("message",message);
        RequestBodyJson.put("receiver",receiver);

        RequestBody body = RequestBody.create(JSON, RequestBodyJson.toString());
        Request request = new Request.Builder()
                .url(BASE_URL + "api/sendMsg")
                .addHeader("Authorization","Bearer " + token)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }


}