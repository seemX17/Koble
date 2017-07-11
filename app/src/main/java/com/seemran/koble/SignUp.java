package com.seemran.koble;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by seemran on 11-Jul-17.
 */

public class SignUp extends AppCompatActivity {

    public Button SignUpBtn;
    public CoordinatorLayout coordinatorSignUp;
    public EditText name,email,password;
    static Typeface customFont;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        customFont = Typeface.createFromAsset(getAssets(), "fonts/montserrat.otf");
        SignUpBtn = (Button) findViewById(R.id.SignUpBtn);
        SignUpBtn.setTypeface(customFont);
        coordinatorSignUp = (CoordinatorLayout) findViewById(R.id.coordinatorSignUp);
        name = (EditText) findViewById(R.id.input_name);
        name.setTypeface(customFont);
        email = (EditText) findViewById(R.id.input_email);
        email.setTypeface(customFont);
        password = (EditText) findViewById(R.id.input_password);
        password.setTypeface(customFont);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textPersonName, emailText, passwordText; // We created two strings, email and password

                textPersonName = name.getText().toString();
                emailText = email.getText().toString(); // We then got the text from Edittext
                passwordText = password.getText().toString();
                if (textPersonName.equals("")||emailText.equals("") || passwordText.equals("")){
                    Toast.makeText(SignUp.this, "Kindly enter all details", Toast.LENGTH_SHORT).show();
                } else{
                    Intent i = new Intent(SignUp.this, Home.class);
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
