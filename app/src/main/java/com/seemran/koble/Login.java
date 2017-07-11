package com.seemran.koble;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    public Button loginBtn;
    public CoordinatorLayout coordinatorlogin;
    public EditText email, password;
    static Typeface customFont;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        customFont = Typeface.createFromAsset(getAssets(), "fonts/montserrat.otf");
        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setTypeface(customFont);
        coordinatorlogin = (CoordinatorLayout) findViewById(R.id.coordinatorLogin);
        email = (EditText) findViewById(R.id.input_email);
        email.setTypeface(customFont);
        password = (EditText) findViewById(R.id.input_password);
        password.setTypeface(customFont);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emailText, passwordText; // We created two strings, email and password

                emailText = email.getText().toString(); // We then got the text from Edittext
                passwordText = password.getText().toString();
                if (emailText.equals("") || passwordText.equals("")){
                    Toast.makeText(Login.this, "Please check your username and password!", Toast.LENGTH_SHORT).show();
                } else{
                    Intent i = new Intent(Login.this, Home.class);
                    startActivity(i);
                }

          //
                //Toast toastname = new Toast(activity, "mesagee", length of toast);
              //  Snackbar snackbarName = Snackbar.make(coordinatorlogin, "Mesage", Snackbar.LENGTH_INDEFINITE);
               // snackbarName.show();
            }
        });
    }
}

