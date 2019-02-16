package mycodlabs.instapp;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.roger.catloadinglibrary.CatLoadingView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mycodlabs.events.R;


public class RegisterActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private Spinner usertypeSpinner;
    private Button bRegister;
    private Context context = null;
    CatLoadingView mView;

    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_event_display_user);
        setContentView(R.layout.registration);
        context = this;
        mView = new CatLoadingView();
        queue = Volley.newRequestQueue(context);
        etName = (EditText) findViewById(R.id.editFullName);
        etEmail = (EditText) findViewById(R.id.editMailAddress);
        etPassword = (EditText) findViewById(R.id.editPasswordS);
        etConfirmPassword = (EditText) findViewById(R.id.editPasswordC);
     //   usertypeSpinner = (Spinner) findViewById(R.id.usertype_spinner);
        bRegister = (Button) findViewById(R.id.signup);
        //getusertype();

     //   TextView loginLink = (TextView) findViewById(R.id.tvSignin) ;
//        loginLink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
//
//                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////
////            // Add new Flag to start new Activity
//                loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                RegisterActivity.this.startActivity(loginIntent);
//
//                finish();
//
//
//            }
//        });

        etName.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (etName.getText().toString().length() == 0){
                    etName.setError("Please enter name");
                }
                else{
                    etName.setError(null);
                }

            }
        });

        etEmail.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
//                ///boolean iitrpr_email = etEmail.getText().toString().matches("[a-zA-Z0-9._-]");
//                if (!iitrpr_email){
//                    etEmail.setError("Invalid Email");
//                }
//                else{
//                    etEmail.setError(null);
//                }

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
                if (etPassword.getText().toString().length() < 1){
                    etPassword.setError("Atleast 1 characters");
                }
                else{
                    etPassword.setError(null);
                }

            }
        });
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (etName.getText().toString().trim().equals("")){
                    Snackbar.make(v, "Please specify Name", Snackbar.LENGTH_SHORT).show();
                }
                else if(etEmail.getText().toString().trim().equals("") || !(etEmail.getText().toString().trim().matches(emailPattern))){
                    Snackbar.make(v, "Please specify valid Email", Snackbar.LENGTH_SHORT).show();
                }
                else if(etPassword.getText().toString().length() < 1){
                    Snackbar.make(v, "Please specify Password of atleast 1 characters", Snackbar.LENGTH_SHORT).show();
                }
                else if(!etPassword.getText().toString().equals(etConfirmPassword.getText().toString()))
                {
                    Snackbar.make(v, "Password Not Matching  ", Snackbar.LENGTH_SHORT).show();
                }
                else{
                    final String name = etName.getText().toString().trim();
                    final String email = etEmail.getText().toString().trim();
//                    final int usertype_id = usertypeSpinner.getSelectedItemPosition()+1;
                    final String password = etPassword.getText().toString();

                          showDialog();

                        StringRequest registerRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.Register_url),
                                new Response.Listener<String>()
                                {
                                    @Override
                                    public void onResponse(String response)
                                    {

                                        try{
                                            Log.d("response",Utils.fromHtml(response));
                                            JSONObject jsonResponse = new JSONObject(response);
                                            boolean success = jsonResponse.getBoolean("success");

                                            if (success) {

                                                 hideDialog();
                                                Toast.makeText(getApplicationContext(), "Register Successful " + email, Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                                // Add new Flag to start new Activity
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                getApplicationContext().startActivity(intent);
                                                finish();

                                            } else {
                                                String msg = jsonResponse.getString("msg");
                                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                              hideDialog();
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

                                Snackbar.make(v, "Couldn't connect to internet", Snackbar.LENGTH_SHORT).show();
                                hideDialog();
                            }
                        }
                        ) {
                            @Override
                            protected Map<String, String> getParams()
                            {
                                Map<String, String>  params = new HashMap<String, String>();
                                params.put("email", email);
                                params.put("password", password);
                                params.put("name", name);
                                params.put("usertype_id", 1+"");

                                return params;
                            }

                        };
                        queue.add(registerRequest);



                }
            }
        });
    }
    @Override
    public void onStop() {
        super.onStop();
        if (queue != null){
            queue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return true;
                }
            });
        }
    }

    private void getusertype(){
            //Creating a string request
        showDialog();

        StringRequest usertypeRequest = new StringRequest(Request.Method.GET, getResources().getString(R.string.usertypeRegister_url),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONObject jsonResponse = null;
                            try {
                                //Parsing the fetched Json String to JSON Object
                                jsonResponse = new JSONObject(response);
                                JSONArray result = jsonResponse.getJSONArray("usertype");
                                ArrayList<String> usertypes = new ArrayList<String>();
                                for(int i=0;i<result.length();i++){

                                        //Getting json object
                                        JSONObject obj = result.getJSONObject(i);

                                        //Adding the name of the student to array list
                                        usertypes.add(obj.getString("name"));

                                }

                              //  usertypeSpinner.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, usertypes));

                                //Calling method getStudents to get the students from the JSON Array
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();

                        }
                    });

            //Creating a request queue

            //Adding request to the queue
            queue.add(usertypeRequest);

    }
    public void showDialog() {
        mView.show(getSupportFragmentManager(), "");
    }
    public void hideDialog() {
        mView.dismiss();
    }
}
