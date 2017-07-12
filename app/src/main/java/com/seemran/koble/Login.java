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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    public Button loginBtn;
    public CoordinatorLayout coordinatorlogin;
    public EditText Username,Password;
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
        Username = (EditText) findViewById(R.id.input_username);
        Username.setTypeface(customFont);
        Password = (EditText) findViewById(R.id.input_password);
        Password.setTypeface(customFont);



    //}

   // @Override
    //protected void onStart() {
    //    super.onStart();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text, passwordText; // We created two strings, email and password

                text = Username.getText().toString(); // We then got the text from Edittext
                passwordText = Password.getText().toString();
                if (text.equals("") || passwordText.equals("")){
                    Toast.makeText(Login.this, "Please check your username and password!", Toast.LENGTH_SHORT).show();
                } else{
                   registerUser();
                }

          //
                //Toast toastname = new Toast(activity, "mesagee", length of toast);
              //  Snackbar snackbarName = Snackbar.make(coordinatorlogin, "Mesage", Snackbar.LENGTH_INDEFINITE);
               // snackbarName.show();
            }
        });
    }

    private void registerUser(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String username = Username.getText().toString().trim();
        final String password = Password.getText().toString().trim();

        String REGISTER_URL ="http://bootcampgoa.com/wp-admin/admin-ajax.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        JSONObject resp = null;
                        try{
                            resp =new JSONObject(response);
                            String message=resp.getString("message");
                            int status=resp.getInt("status");
                            Toast.makeText(Login.this,message,Toast.LENGTH_LONG).show();
                            if(message.equals("Success")){
                            Intent i = new Intent(Login.this, Home.class);
                            startActivity(i);}

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Login.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                params.put("action","check_user");
                params.put("username",username);
                params.put("password", password);
                return params;
            }

        };


        requestQueue.add(stringRequest);
    }
}
