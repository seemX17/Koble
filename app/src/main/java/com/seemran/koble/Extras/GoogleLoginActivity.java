package com.seemran.koble.Extras;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.seemran.koble.Activity.HomeActitvity;
import com.seemran.koble.Activity.RegisterActivity;
import com.seemran.koble.R;

public class GoogleLoginActivity extends AppCompatActivity implements View.OnClickListener,  GoogleApiClient.OnConnectionFailedListener {

    public Button loginbtn;
    public CoordinatorLayout coordinatorlogin;
    public TextView email,name;
    public static Typeface customFont;
    public TextView register;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public SignInButton sign_in_button;
    public LinearLayout profile_section;
    public GoogleApiClient googleApiClient;
    public ImageView profilepic;
    public static final int REQ_CODE= 9001;

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
        email = (TextView) findViewById(R.id.input_email);
        email.setTypeface(customFont);
        name = (TextView) findViewById(R.id.input_name);
        name.setTypeface(customFont);
        register = (TextView)findViewById(R.id.reglink);
        register.setTypeface(customFont);
        profile_section=(LinearLayout)findViewById((R.id.profile_section));
        sign_in_button=(SignInButton)findViewById(R.id.sign_in_button);
        profilepic=(ImageView)findViewById(R.id.profile_pic);


        loginbtn.setOnClickListener(this);
        sign_in_button.setOnClickListener(this);
        profile_section.setVisibility(View.GONE);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();//take in email
        googleApiClient=new GoogleApiClient.Builder(this).
                enableAutoManage(this,this).
                addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).
                build();//signinapi

//        //<-----SHARED PREFERENCES---->
//        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
//        String restoredText = prefs.getString("email", null);
//        if (restoredText != null) {
//                                                                                                                                                                                              /*  String loadusername = prefs.getString("Username", "No name defined");//"No name defined" is the default value.
//                                                                                                                                                                                                String loadpassword = prefs.getString("Password", " "); //0 is the default value.
//                                                                                                                                                                                                Username.setText(loadusername);
//                                                                                                                                                                                                Password.setText(loadpassword);*/
//            Intent i = new Intent(GoogleLoginActivity.this,HomeActitvity.class);
//            startActivity(i);
//
//        }

        //}

        // @Override
        //protected void onStart() {
        //    super.onStart();

        //    //<------VALIDATION---->
        //    loginBtn.setOnClickListener(new View.OnClickListener() {
        //        @Override
        //        public void onClick(View view) {
        //
        //            String text, passwordText; // We created two strings, email and password
        //
        //            text = email.getText().toString(); // We then got the text from Edittext
        //            passwordText = Password.getText().toString();
        //            if (text.equals("") || passwordText.equals("")){
        //                Toast.makeText(GoogleLoginActivity.this, "Please check your username and password!", Toast.LENGTH_SHORT).show();
        //            } else{
        //              // loginuser();
        //                Intent i = new Intent(GoogleLoginActivity.this,HomeActitvity.class);
        //                startActivity(i);
        //            }
        //        }
        //    });



        //<-----REGISTER LINK---->
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GoogleLoginActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });

        //Toast toastname = new Toast(activity, "mesagee", length of toast);
        //  Snackbar snackbarName = Snackbar.make(coordinatorlogin, "Mesage", Snackbar.LENGTH_INDEFINITE);
        // snackbarName.show();
    }

    @Override
    public void onClick(View view) {

        switch(view.getId())
        {
            case R.id.sign_in_button:
                SignInButton();
                break;
            case R.id.loginbtn :
//                loginbutton();
                loginbtn.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View view) {
                        Intent i = new Intent(GoogleLoginActivity.this,HomeActitvity.class);
                        startActivity(i);
                    }
                });

                break;

        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public void SignInButton(){
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);

    }

//    public void signoutbutton()
//    {
//        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
//            @Override
//            public void onResult(@NonNull Status status) {
//                updateui(false);
//            }
//        });
//
//    }


    public void handleResult(GoogleSignInResult result){
        if (result.isSuccess())
        {
            GoogleSignInAccount account = result.getSignInAccount();
            String useremail = account.getEmail();
            String username = account.getDisplayName();
            String img_url = account.getPhotoUrl().toString();
            name.setText(username);
            email .setText(useremail);
            Glide.with(this).load(img_url).into(profilepic);
            updateui(true);
        }
        else
        {
            updateui(false);
        }

    }


    public void updateui (boolean islogin){

        if(islogin){
            profile_section.setVisibility(View.VISIBLE);
            sign_in_button.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //for recieveing the result
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQ_CODE)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
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

