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

public class LoginActivity extends AppCompatActivity {
    Button loginButton;
    EditText usernameEditText;
    TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        loginButton = (Button) findViewById(R.id.loginButton);
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
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
        if(usernameEditText.getText().toString().equals("")){
            Intent intent = new Intent(LoginActivity.this,
                    HomeActivity.class);
            LoginActivity.this.startActivity(intent);
        }
        else{
            loginButton.setBackgroundColor(Color.RED);
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Bad ID",
                    Toast.LENGTH_SHORT);

            toast.show();
        }
    }


}
