package com.mbds.geoffreyroman.messagerie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class RegisterActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button button;
    private TextView message;
    Database db;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        db = Database.getInstance(getApplicationContext());
        username = findViewById(R.id.usernameRegistre);
        password = findViewById(R.id.passwordRegistre);
        button = findViewById(R.id.registerButton);
        message = findViewById(R.id.registerMessage);
        context = getApplicationContext();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRegister();
            }
        });

    }

    void checkRegister() {

        String username = this.username.getText().toString();
        String password = this.password.getText().toString();

        if (username.length() == 0 || password.length() == 0) {
            Toast.makeText(context,
                    "Error register", Toast.LENGTH_SHORT).show();
        } else {
            createUser(username, password);
        }
    }

    private void setMessage(final String message_) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView message = (TextView) findViewById(R.id.registerMessage);
                message.setText(message_);
            }
        });
    }

    private void createUser(String username, String password) {
        db.createUser(username, password,
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        setMessage("HTTP error 1");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        System.out.println("GOOOOD");
                        System.out.println(response);

                        if (response.isSuccessful()) {
                            String responseStr = response.body().string();
                            setMessage("Inscription réussite");
                            Intent intent = new Intent(RegisterActivity.this,
                                    LoginActivity.class);
                            RegisterActivity.this.startActivity(intent);
                            //Toast.makeText(context,
                            //      "Register succesfull", Toast.LENGTH_SHORT).show();
                        } else {
                            setMessage("HTTP error 2");
                        }
                    }
                });
    }
}
