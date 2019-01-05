package com.mbds.geoffreyroman.messagerie;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                login();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,
                        RegisterActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });
    }

    private void setMessage(final String message_) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView message = (TextView) findViewById(R.id.loginMessage);
                message.setText(message_);
                if(message_.equals("Bad Id")){
                    Button loginButton = (Button) findViewById(R.id.loginButton);
                    loginButton.setBackgroundColor(Color.RED);
                }
            }
        });
    }

    void login() {
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

                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {

                            Crypto c = new Crypto("alias");
                            c.getPublicKey("alias");
                            byte[] messageCrypte = c.crypte("Message a crypter");
                            c.decrypte(messageCrypte);

                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (NoSuchPaddingException e) {
                            e.printStackTrace();
                        } catch (InvalidKeyException e) {
                            e.printStackTrace();
                        } catch (BadPaddingException e) {
                            e.printStackTrace();
                        } catch (IllegalBlockSizeException e) {
                            e.printStackTrace();
                        } catch (InvalidAlgorithmParameterException e) {
                            e.printStackTrace();
                        } catch (NoSuchProviderException e) {
                            e.printStackTrace();
                        } catch (CertificateException e) {
                            e.printStackTrace();
                        } catch (KeyStoreException e) {
                            e.printStackTrace();
                        } catch (UnrecoverableEntryException e) {
                            e.printStackTrace();
                        }

                        if (response.isSuccessful()) {
                            try {
                                String jsonData = response.body().string();
                                JSONObject userInfoJson = new JSONObject(jsonData);
                                db.setUserInfo(userInfoJson);
                                // JSONArray Jarray = userInfoJson.getJSONArray("access_token");
                                setMessage("Connexion réussite");
                                Intent intent = new Intent(LoginActivity.this,
                                        HomeActivity.class);
                                // intent.putExtra("database",  db.getINSTANCE());
                                LoginActivity.this.startActivity(intent);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        } else {
                            setMessage("Bad Id");
                        }
                    }
                }
        );

    }


}
