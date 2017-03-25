package com.example.ariel.boodal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ariel.boodal.Object.UserObject;
import com.example.ariel.boodal.helper.AppController;
import com.example.ariel.boodal.helper.SQLiteHandler;
import com.example.ariel.boodal.helper.SessionManager;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private TextView btnregis;
    private Button btnlogin;
    private EditText inputEmail,inputPassword;
    private ArrayList<UserObject> allItems = new ArrayList<UserObject>();
    private String urlJsonObj = "http://mlogg.com/boodal/public/api/auth/login";
    private ProgressDialog pDialog;
    private String jsonResponse;
    private SQLiteHandler db;
    private SessionManager session;
    private String email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(Login.this, User_Drawer.class);
            startActivity(intent);
            finish();
        }

        btnlogin = (Button) findViewById(R.id.btn_login);
        inputEmail = (EditText) findViewById(R.id.input_username);
        inputPassword = (EditText) findViewById(R.id.input_pass);


        btnregis = (TextView) findViewById(R.id.signup);
        btnregis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this,Register.class);
                startActivity(i);
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                email = inputEmail.getText().toString().trim();
                password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    urlJsonObj ="http://mlogg.com/boodal/public/api/auth/login";
                    checkLogin(email, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the form!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });
    }

    private void checkLogin(final String email,final String password) {
        String string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();
        JsonObjectRequest strReq = new JsonObjectRequest (Request.Method.POST,urlJsonObj,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("error :", "Login Response: " + response.toString());
                hideDialog();
                try {
                    int error = response.getInt("status_code");
                    JSONArray setdata = new JSONArray();
                    // Check for error node in json
                    if (error == 200) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);
                        // Now store the user in SQLite
                        setdata = response.getJSONArray("data");
                        for (int i = 0; i < setdata.length(); i++) {
                            JSONObject data = setdata.getJSONObject(i);
                            String id = data.getString("id");
                            String name =data.getString("name");
                            String email = data.getString("email");
                            String phone = data.getString("phone");
                            int status = data.getInt("status");
                            double lat = data.getDouble("lat");
                            double lng = data.getDouble("lng");
                            String api_token = data.getString("api_token");
                            db.addUser(id,name, email,phone,status,lat,lng,api_token);
                        }
                        // Launch main activity
                        Intent intent = new Intent(Login.this,User_Drawer.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = "Email dan Password salah";
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("pass", password);
                return params;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("Accept","application/x-www-form-urlencoded");
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
