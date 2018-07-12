package me.snyde606.parstagram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    private EditText etUsernameSign;
    private EditText etPasswordSign;
    private Button btCompleteSignUp;
    private EditText etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etUsernameSign = findViewById(R.id.etUsernameSign);
        etPasswordSign = findViewById(R.id.etPasswordSign);
        btCompleteSignUp = findViewById(R.id.btCompleteSignUp);
        etEmail = findViewById(R.id.etEmail);

        btCompleteSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser user = new ParseUser();
// Set core properties
                user.setUsername(etUsernameSign.getText().toString());
                user.setPassword(etPasswordSign.getText().toString());
                user.setEmail(etEmail.getText().toString());
// Invoke signUpInBackground
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        finish();
                    }
                });
            }
        });

    }

}
