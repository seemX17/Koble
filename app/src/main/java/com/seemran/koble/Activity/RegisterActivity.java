package com.seemran.koble.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.seemran.koble.R;

public class RegisterActivity extends AppCompatActivity {

    public Button RegBtn;
    public CoordinatorLayout coordinatorsignup;
    public EditText username, password, email;
    public Typeface customFont;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private  Activity ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        Toolbar regtool = (Toolbar) findViewById(R.id.regtoolbar);
        setSupportActionBar(regtool);
        customFont = Typeface.createFromAsset(getAssets(), "fonts/montserrat.otf");
        RegBtn = (Button) findViewById(R.id.RegBtn);
        RegBtn.setTypeface(customFont);
        coordinatorsignup = (CoordinatorLayout) findViewById(R.id.coordinatorsignup);
//        username = (EditText) findViewById(R.id.regusername);
//        username.setTypeface(customFont);
        password = (EditText) findViewById(R.id.regpassword);
        password.setTypeface(customFont);
        email = (EditText) findViewById(R.id.regemail);
        email.setTypeface(customFont);


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("operations", "onAuthStateChanged:signed_in:" + user.getUid());//debug
                } else {
                    // User is signed out
                    Log.d("operations", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        ctx=this;
        RegBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()) .addOnCompleteListener(ctx, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d("Operations", "createUserWithEmail:onComplete:" + task.isSuccessful());
                                Toast.makeText(ctx,"Signup successfull",Toast.LENGTH_SHORT).show();
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(ctx,"Sign up failed",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Intent i = new Intent(RegisterActivity.this,HomeActitvity.class);
                                    startActivity(i);

                                }
                            }
                        });
            }
        });

//
//        RegBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String  emailtext, passwordtext;
//                emailtext = email.getText().toString();
//                passwordtext = password.getText().toString();
//
//                if ( emailtext.equals("") || passwordtext.equals("")) {
//                    Toast.makeText(RegisterActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
//              }
//              //else {
///                  Intent i = new Intent(RegisterActivity.this, HomeActitvity.class);
//                          startActivity(i);;
//                }
//            }
//
//        });
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }




//    private void registeruser() {
//            RequestQueue regrequest = Volley.newRequestQueue(this);
//            final String requsername = username.getText().toString().trim(); //Trim fuction learn
//            final String reqpassword = password.getText().toString().trim();
//            final String reqemail = emailid.getText().toString().trim();
//
//            String REGISTER_URL = "http://bootcampgoa.com/wp-admin/admin-ajax.php";
//            StringRequest reqstring = new StringRequest(Request.Method.POST, REGISTER_URL, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    JSONObject resp = null;
//                    try {
//                        resp = new JSONObject(response);
//                        String message = resp.getString("message");
//                        int status = resp.getInt("status");
//                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
//                        if (message.equals("User Exists")) {
//                            Intent i = new Intent(RegisterActivity.this, HomeActitvity.class);
//                            startActivity(i);
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//
//                }
//            },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();
//                        }
//                    }) {
//                @Override
//                protected Map<String, String> getParams() {
//                    Map<String, String> params = new HashMap<String, String>();
//
//                    params.put("action", "register_user");
//                    params.put("username", requsername);
//                    params.put("password", reqpassword);
//                    params.put("user_email", reqemail);
//                    return params;
//                }
//
//
//            };
//            regrequest.add(reqstring);
//        }
}

