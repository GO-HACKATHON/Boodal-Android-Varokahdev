package com.example.ariel.boodal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ariel.boodal.helper.AppController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.ariel.boodal.Object.UserObject;
import com.example.ariel.boodal.helper.SQLiteHandler;
import com.example.ariel.boodal.helper.SessionManager;

public class Register extends AppCompatActivity {

    private TextView btnlogin;
    private Button btnregister;
    private EditText inputuser,inputPassworduser,inputconfirmpassword;
    private ArrayList<UserObject> allItems = new ArrayList<UserObject>();
    private String urlJsonObj = "http://mlogg.com/boodal/public/api/auth/login";
    private ProgressDialog pDialog;
    private String jsonResponse;
    private SQLiteHandler db;
    private SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(Register.this,
                    User_Drawer.class);
            startActivity(intent);
            finish();
        }

        inputuser = (EditText) findViewById(R.id.input_username);
        inputPassworduser = (EditText) findViewById(R.id.textpassuser);
        inputconfirmpassword = (EditText) findViewById(R.id.confirmpass);

        btnlogin = (TextView) findViewById(R.id.signup);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Register.this,Login.class);
                startActivity(i);
            }
        });
        btnregister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String username = inputuser.getText().toString().trim();
                String password = inputPassworduser.getText().toString().trim();
                String confirmpassword = inputconfirmpassword.getText().toString().trim();


                if (!username.isEmpty() && !password.isEmpty() && !confirmpassword.isEmpty()) {
                    urlJsonObj = "http://mlogg.com/boodal/public/api/auth/register";
                    registerUser();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }
    private void registerUser() {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        JSONObject jsonObject1=new JSONObject();


        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET, urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Test", "Register Response: " + response.toString());
                hideDialog();

                try {
                    String status = response.getString("status");
                    if (status.equals("1")) {
                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();
                        // Launch login activity
                        Intent intent = new Intent(
                                Register.this,
                                Login.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = response.getString("status");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("erorr :", "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                return params;
            }
        };

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
