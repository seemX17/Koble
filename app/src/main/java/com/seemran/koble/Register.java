package com.seemran.koble;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    public Button RegBtn;
    public CoordinatorLayout coordinatorsignup;
    public EditText username,password,emailid;
    public Typeface customFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar regtool = (Toolbar) findViewById(R.id.regtoolbar);
        setSupportActionBar(regtool);
        customFont = Typeface.createFromAsset(getAssets(), "fonts/montserrat.otf");
        RegBtn = (Button) findViewById(R.id.RegBtn);
        RegBtn.setTypeface(customFont);
        coordinatorsignup = (CoordinatorLayout) findViewById(R.id.coordinatorsignup);
        username = (EditText) findViewById(R.id.regusername);
        username.setTypeface(customFont);
        password = (EditText) findViewById(R.id.regpassword);
        password.setTypeface(customFont);
        emailid = (EditText) findViewById(R.id.regemail);
        emailid.setTypeface(customFont);


        RegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textname, emailtext, passwordtext;
                textname = username.getText().toString();
                emailtext = emailid.getText().toString();
                passwordtext = password.getText().toString();

                if (textname.equals("") || emailtext.equals("") || passwordtext.equals("")) {
                    Toast.makeText(Register.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(Register.this, Home.class);
                    startActivity(i);
                }
            }

        });

    }

}

