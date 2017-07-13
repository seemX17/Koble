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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    public Button RegBtn;
    public CoordinatorLayout coordinatorsignup;
    public EditText username, password, emailid;
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
                    registeruser();
                }
            }

        });

    }




    private void registeruser() {
            RequestQueue regrequest = Volley.newRequestQueue(this);
            final String requsername = username.getText().toString().trim(); //Trim fuction learn
            final String reqpassword = password.getText().toString().trim();
            final String reqemail = emailid.getText().toString().trim();

            String REGISTER_URL = "http://bootcampgoa.com/wp-admin/admin-ajax.php";
            StringRequest reqstring = new StringRequest(Request.Method.POST, REGISTER_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    JSONObject resp = null;
                    try {
                        resp = new JSONObject(response);
                        String message = resp.getString("message");
                        int status = resp.getInt("status");
                        Toast.makeText(Register.this, message, Toast.LENGTH_LONG).show();
                        if (message.equals("User Exists")) {
                            Intent i = new Intent(Register.this, Home.class);
                            startActivity(i);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Register.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("action", "register_user");
                    params.put("username", requsername);
                    params.put("password", reqpassword);
                    params.put("user_email", reqemail);
                    return params;
                }


            };
            regrequest.add(reqstring);
        }
}

