package com.mbds.geoffreyroman.messagerie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterActivity  extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button button;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        username = findViewById(R.id.usernameRegistre);
        password = findViewById(R.id.passwordRegistre);
        button = findViewById(R.id.registerButton);
        context = getApplicationContext();

        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRegister();
            }
        });

    }

    void checkRegister(){

        String username = this.username.getText().toString();
        String password = this.password.getText().toString();

        if(username.length() == 0 || password.length() == 0){
            Toast.makeText(context,
                    "Error register", Toast.LENGTH_SHORT).show();
        }
        else{
            createUser(username, password);
        }
    }

    private void createUser(String username, String password) {

        JSONObject jo = null;

        try
        {
            jo = new JSONObject();
            jo.put("username", username);
            jo.put("password", password);
        } catch (JSONException JsonE) {
            JsonE.printStackTrace();
        }

        String params = jo.toString();
        ApiService api = new ApiService();
        try {
            api.createUser(params,
                    new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Toast.makeText(context,
                                    "HTTP error 1", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            System.out.println("GOOOOD");
                            System.out.println(response);

                            if (response.isSuccessful()) {
                                String responseStr = response.body().string();
                                Intent intent = new Intent(RegisterActivity.this,
                                        LoginActivity.class);
                                RegisterActivity.this.startActivity(intent);
                                //Toast.makeText(context,
                                  //      "Register succesfull", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context,
                                        "HTTP error 2", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
