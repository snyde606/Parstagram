package me.snyde606.parstagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.etUsername);
        passwordInput = findViewById(R.id.etPassword);
        loginButton = findViewById(R.id.btLogin);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {

            final Intent intent = new Intent(LoginActivity.this, ComposeActivity.class);
            startActivity(intent);
            finish();

        }

        loginButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                final String username = usernameInput.getText().toString();
                final String password = passwordInput.getText().toString();

                login(username, password);
            }
        });
    }

    private void login(String username, String password){
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null){
                    Log.d("LogInActivity", "Login successful!");
                    final Intent intent = new Intent(LoginActivity.this, ComposeActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Log.d("LogInActivity", "Login failure!");
                    e.printStackTrace();
                }
            }
        });
    }
}
