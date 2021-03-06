package mycodlabs.instapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.roger.catloadinglibrary.CatLoadingView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import mycodlabs.events.NavBar;
import mycodlabs.events.R;
import mycodlabs.events.UserSessionManager;


//import com.google.firebase.messaging.FirebaseMessaging;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail;
    EditText etPassword;
    Button bLogin;
    Button registerLink;
    UserSessionManager session;
    RequestQueue queue;
    CatLoadingView mView;
    TextView gustLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login);

        // Session Manager
        session = new UserSessionManager(getApplicationContext());
        queue = Volley.newRequestQueue(getApplicationContext());
        mView = new CatLoadingView();
        etEmail = (EditText) findViewById(R.id.username);
        etPassword = (EditText) findViewById(R.id.password);
        bLogin = (Button) findViewById(R.id.singin);
        registerLink = (Button) findViewById(R.id.signups);
        gustLogin=(TextView)findViewById(R.id.text_gust);




        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);

                registerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//            // Add new Flag to start new Activity
                registerIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                LoginActivity.this.startActivity(registerIntent);

                finish();


            }
        });

        if (session.isUserLoggedIn()){
            Intent intent = new Intent(LoginActivity.this, NavBar.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            LoginActivity.this.startActivity(intent);
            finish();
      }




        etEmail.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
//                boolean iitrpr_email = etEmail.getText().toString().matches("[a-zA-Z0-9._-]+@iitrpr\\.ac\\.in");
//                if (!iitrpr_email){
//                    etEmail.setError("Invalid Email");
//                }
//                else{
//                    etEmail.setError(null);
//                }
//
            }
        });



        etPassword.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (etPassword.getText().toString().length() == 0){
                    etPassword.setError("Please enter Password");
                }
                else{
                    etPassword.setError(null);
                }

            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(etEmail.getText().toString().trim().equals("")){
                    Snackbar.make(v, "Please specify Username", Snackbar.LENGTH_SHORT).show();
                }
                else {
                    if (etPassword.getText().toString().equals("")) {
                        Snackbar.make(v, "Please specify Password", Snackbar.LENGTH_SHORT).show();
                    } else {
                        final String email = etEmail.getText().toString().trim();
                        final String password = etPassword.getText().toString();

                        showDialog();
                        StringRequest loginRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.Login_url),
                                new Response.Listener<String>()
                                {
                                    @Override
                                    public void onResponse(String response)
                                    {
                                        try{
                                            Log.d("response",response.toString());
                                            final JSONObject jsonResponse = new JSONObject(response);
                                            boolean success = jsonResponse.getBoolean("success");
                                            if (success) {
                                                UserSessionManager session = new UserSessionManager(getApplicationContext());
                                                session.createUserLoginSession(jsonResponse.getString("name"), jsonResponse.getString("email"), jsonResponse.getInt("user_id"), jsonResponse.getInt("usertype_id"), jsonResponse.getString("usertype"),jsonResponse.getInt("usertypes"), jsonResponse.getString("created"));

//                                                FirebaseMessaging.getInstance().subscribeToTopic("0");
//                                                for (int i=1; i<jsonResponse.getInt("usertypes");i++){
//                                                    if (i==jsonResponse.getInt("usertype_id")){
//                                                        FirebaseMessaging.getInstance().subscribeToTopic(i+"");
//                                                    }
//                                                    else{
//                                                        FirebaseMessaging.getInstance().unsubscribeFromTopic(i+"");
//                                                    }
//                                                }/
                                                hideDialog();
                                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.firebase_pref),MODE_PRIVATE);
                                                final int user_id = jsonResponse.getInt("user_id");
                                                final String token = sharedPreferences.getString(getString(R.string.firebase_token),"");
                                                StringRequest fcmRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.updateFCMtoken_url),
                                                        new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {

                                                                //                        try{
                                                                //                            JSONObject jsonResponse = new JSONObject(response);
                                                                //                            boolean success = jsonResponse.getBoolean("success");
                                                                //
                                                                //                            if (success) {
                                                                //
                                                                //
                                                                //
                                                                //                            } else {
                                                                //
                                                                //
                                                                //                            }
                                                                //                        }catch (JSONException e) {
                                                                //                            e.printStackTrace();
                                                                //                        }

                                                            }
                                                        }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        // error

                                                    }
                                                }
                                                ) {
                                                    @Override
                                                    protected Map<String, String> getParams() {
                                                        Map<String, String> params = new HashMap<String, String>();
                                                        params.put("user_id", user_id+"");
                                                        params.put("token", token);


                                                        return params;
                                                    }

                                                };

                                                queue.add(fcmRequest);



                                                Toast.makeText(getApplicationContext(), "Login Successful " + jsonResponse.getString("name"), Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(LoginActivity.this, NavBar.class);

                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                                // Add new Flag to start new Activity
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                LoginActivity.this.startActivity(intent);
                                                finish();

                                            } else {
                                                hideDialog();
                                                Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();

                                            }
                                        }catch (JSONException e) {
                                            e.printStackTrace();

                                        }

                                    }
                                }, new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                hideDialog();
                                Snackbar.make(v, "Couldn't connect to internet", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                        ) {
                            @Override
                            protected Map<String, String> getParams()
                            {
                                Map<String, String>  params = new HashMap<String, String>();
                                params.put("email", email);
                                params.put("password", password);

                                return params;
                            }

                        };
                        queue.add(loginRequest);
                    }
                }

            }
        });

        gustLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                {
                        final String email ="gust@test.com";
                        final String password = "123456789";

                        showDialog();
                        StringRequest loginRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.Login_url),
                                new Response.Listener<String>()
                                {
                                    @Override
                                    public void onResponse(String response)
                                    {
                                        try{
                                            Log.d("response",response.toString());
                                            final JSONObject jsonResponse = new JSONObject(response);
                                            boolean success = jsonResponse.getBoolean("success");
                                            if (success) {
                                                UserSessionManager session = new UserSessionManager(getApplicationContext());
                                                session.createUserLoginSession(jsonResponse.getString("name"), jsonResponse.getString("email"), jsonResponse.getInt("user_id"), jsonResponse.getInt("usertype_id"), jsonResponse.getString("usertype"),jsonResponse.getInt("usertypes"), jsonResponse.getString("created"));

//                                                FirebaseMessaging.getInstance().subscribeToTopic("0");
//                                                for (int i=1; i<jsonResponse.getInt("usertypes");i++){
//                                                    if (i==jsonResponse.getInt("usertype_id")){
//                                                        FirebaseMessaging.getInstance().subscribeToTopic(i+"");
//                                                    }
//                                                    else{
//                                                        FirebaseMessaging.getInstance().unsubscribeFromTopic(i+"");
//                                                    }
//                                                }/
                                                hideDialog();
                                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.firebase_pref),MODE_PRIVATE);
                                                final int user_id = jsonResponse.getInt("user_id");
                                           //     final String token = sharedPreferences.getString(getString(R.string.firebase_token),"");


                                                Toast.makeText(getApplicationContext(), "Login Successful " + jsonResponse.getString("name"), Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(LoginActivity.this, NavBar.class);

                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                                // Add new Flag to start new Activity
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                LoginActivity.this.startActivity(intent);
                                                finish();

                                            } else {
                                                hideDialog();
                                                Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();

                                            }
                                        }catch (JSONException e) {
                                            e.printStackTrace();

                                        }

                                    }
                                }, new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                hideDialog();
                                Snackbar.make(v, "Couldn't connect to internet", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                        ) {
                            @Override
                            protected Map<String, String> getParams()
                            {
                                Map<String, String>  params = new HashMap<String, String>();
                                params.put("email", email);
                                params.put("password", password);

                                return params;
                            }

                        };
                        queue.add(loginRequest);
                    }
                }


        });
    
    }



    public void showDialog() {
        mView.show(getSupportFragmentManager(), "");
    }
    public void hideDialog() {
        mView.dismiss();
    }

}
