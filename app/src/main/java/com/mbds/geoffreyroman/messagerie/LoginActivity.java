package com.mbds.geoffreyroman.messagerie;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    Button loginButton;
    EditText usernameEditText;
    EditText passwordEditText;
    TextView register;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        db = Database.getInstance(getApplicationContext());
        loginButton = (Button) findViewById(R.id.loginButton);
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        register = (TextView) findViewById(R.id.registerLink);

        loginButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                login();
            }
        });

        register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,
                        RegisterActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });
    }

    void login(){
        db.checkUser(usernameEditText.getText().toString(), passwordEditText.getText().toString(),
                    new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            // TODO diplay error message
                            loginButton.setBackgroundColor(Color.RED);
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Bad ID",
                                    Toast.LENGTH_SHORT);

                            toast.show();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            System.out.println("GOOOOD");
                            System.out.println(response);

                            if (response.isSuccessful()) {
                                String res = response.body().string();
                                Intent intent = new Intent(LoginActivity.this,
                                        HomeActivity.class);
                                LoginActivity.this.startActivity(intent);
                            } else {
                                // TODO diplay error message
                                loginButton.setBackgroundColor(Color.RED);
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Bad ID",
                                        Toast.LENGTH_SHORT);

                                toast.show();

                            }
                        }
                    }
                    );

    }


}
