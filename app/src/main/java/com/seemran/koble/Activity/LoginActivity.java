package com.seemran.koble.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.seemran.koble.ChatPubnub.Constants;
import com.seemran.koble.ChatPubnub.ChatActivity;
import com.seemran.koble.R;

public class LoginActivity extends AppCompatActivity{

    public Button loginbtn;
    public CoordinatorLayout coordinatorlogin;
    public EditText username,password;
    public static Typeface customFont;
    public TextView register;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        customFont = Typeface.createFromAsset(getAssets(), "fonts/montserrat.otf");
        loginbtn = (Button) findViewById(R.id.loginbtn);
        loginbtn.setTypeface(customFont);
        coordinatorlogin = (CoordinatorLayout) findViewById(R.id.coordinatorLogin);
        username = (EditText) findViewById(R.id.input_username);
        username.setTypeface(customFont);
        password = (EditText) findViewById(R.id.input_password);
        password.setTypeface(customFont);
        register = (TextView)findViewById(R.id.reglink);
        register.setTypeface(customFont);

//        //<-----SHARED PREFERENCES---->
//        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
//        String restoredText = prefs.getString("email", null);
//        if (restoredText != null) {
//      /*  String loadusername = prefs.getString("Username", "No name defined");//"No name defined" is the default value.
//        String loadpassword = prefs.getString("Password", " "); //0 is the default value.
//        Username.setText(loadusername);
//        Password.setText(loadpassword);*/
//            Intent i = new Intent(LoginActivity.this,HomeActitvity.class);
//            startActivity(i);




        //<l---------loginpubnub----->
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            String lastUsername = extras.getString("oldUsername", "");
            username.setText(lastUsername);
        }


        }//        <--onCreateClose-->


         @Override
        protected void onStart() {
            super.onStart();

            //<------VALIDATION---->
            loginbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

 //                 String text, passwordText; // We created two strings, email and password
//
//                    text = username.getText().toString(); // We then got the text from Edittext
//                    passwordText = password.getText().toString();
//                    if (text.equals("") || passwordText.equals("")){
//                        Toast.makeText(LoginActivity.this, "Please check your username and password!", Toast.LENGTH_SHORT).show();
//                    } else{
//
//                        Intent i = new Intent(LoginActivity.this,HomeActitvity.class);
//                        startActivity(i);
//                    }



                    //<----loginpubnub--->
                    String musername = username.getText().toString();
                    if (!validUsername(musername))
                        return;

                    SharedPreferences sp = getSharedPreferences(Constants.CHAT_PREFS,MODE_PRIVATE);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString(Constants.CHAT_USERNAME, musername);
                    edit.apply();

                    Intent intent = new Intent(LoginActivity.this, HomeActitvity.class);
                    startActivity(intent);
                }
            });



        //<-----REGISTER LINK---->
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });

//        Toast toastname = new Toast(, "mesagee", length of toast);
//          Snackbar snackbarName = Snackbar.make(coordinatorlogin, "Mesage", Snackbar.LENGTH_INDEFINITE);
//         snackbarName.show();
    }
    /**
     * Optional function to specify what a username in your chat app can look like.
     * @param musername The name entered by a user.
     * @return
     */
    private boolean validUsername(String musername) {
        if (username.length() == 0) {
            username.setError("Username cannot be empty.");
            return false;
        }
        if (username.length() > 16) {
            username.setError("Username too long.");
            return false;
        }
        return true;
    }

    //    private void loginuser(){
    //        RequestQueue requestQueue = Volley.newRequestQueue(this);
    //        final String username = Username.getText().toString().trim();
    //        final String password = Password.getText().toString().trim();
    //
    //        String LOGIN_URL ="http://bootcampgoa.com/wp-admin/admin-ajax.php";
    //        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,new Response.Listener<String>() {
    //                    @Override
    //                    public void onResponse(String response) {
    //
    //
    //                        JSONObject resp = null;
    //                        try{
    //                            resp =new JSONObject(response);
    //                            String message=resp.getString("message");
    //                            int status=resp.getInt("status");
    //                            Toast.makeText(GoogleLoginActivity.this,message,Toast.LENGTH_LONG).show();
    //                            if(message.equals("Success")){
    //                            Intent i = new Intent(GoogleLoginActivity.this, HomeActitvity.class);
    //
    //                                //<shared preferences>
    //                                SharedPreferences.Editor editor = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE).edit();
    //                                editor.putString("Username", username);
    //                                editor.putString("Password",password);
    //                               // editor.putBoolean("Remember",true);
    //                                editor.commit();
    //
    //                                startActivity(i);
    //                            }
    //
    //                        }
    //                        catch (JSONException e){
    //                            e.printStackTrace();
    //                        }
    //
    //                    }
    //                },
    //                new Response.ErrorListener() {
    //                    @Override
    //                    public void onErrorResponse(VolleyError error) {
    //                        Toast.makeText(GoogleLoginActivity.this,error.toString(),Toast.LENGTH_LONG).show();
    //                    }
    //                }){
    //            @Override
    //            protected Map<String,String> getParams(){
    //                Map<String,String> params = new HashMap<String, String>();
    //
    //                params.put("action","check_user");
    //                params.put("username",username);
    //                params.put("password", password);
    //                return params;
    //        }
    //
    //        };
    //
    //
    //
    //        requestQueue.add(stringRequest);
    //        }
}

