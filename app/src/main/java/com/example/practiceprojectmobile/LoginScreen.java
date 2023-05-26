package com.example.practiceprojectmobile;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.practiceprojectmobile.authentication.AuthenticationResponse;
import com.example.practiceprojectmobile.models.Role;
import com.example.practiceprojectmobile.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LoginScreen extends AppCompatActivity {

    private Dialog registerPopup;
    private RequestQueue mRequestQueue;
    private EditText usernameLoginField;
    private EditText passwordLoginField;
    private EditText registerAgeField;
    private EditText registerLastNameField;
    private EditText registerUsernameField;
    private EditText registerPasswordField;
    private EditText registerFirstNameField;
    private AuthenticationResponse authResponse;
    private Dialog loadingScreenPopup;
    private String authenticateApiEndpoint;
    private String registerApiEndpoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        instantiate();
    }

    private void instantiate(){
        registerPopup = new Dialog(LoginScreen.this);
        authenticateApiEndpoint = "https://practice-api-service-practice-api-recipes.azuremicroservices.io/authenticate";
        registerApiEndpoint = "https://practice-api-service-practice-api-recipes.azuremicroservices.io/register";
        usernameLoginField = findViewById(R.id.usernameLoginField);
        passwordLoginField = findViewById(R.id.passwordLoginField);
        AppCompatButton loginButton = findViewById(R.id.loginButton);
        AppCompatButton registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterPopup();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    login();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    public void showRegisterPopup(){
        registerPopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        registerPopup.setCancelable(true);
        registerPopup.setContentView(R.layout.register_popup);
        registerUsernameField = registerPopup.findViewById(R.id.registerUsernameField);
        registerPasswordField = registerPopup.findViewById(R.id.registerPasswordField);
        registerFirstNameField = registerPopup.findViewById(R.id.registerFirstNameField);
        registerLastNameField = registerPopup.findViewById(R.id.registerLastNameField);
        registerAgeField = registerPopup.findViewById(R.id.registerAgeField);
        AppCompatButton submitRegisterButton = registerPopup.findViewById(R.id.submitRegisterButton);
        submitRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    registerNewUser();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        registerPopup.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                registerPopup = new Dialog(LoginScreen.this);
            }
        });

        registerPopup.show();

    }
    public void registerNewUser() throws JSONException {
        mRequestQueue = Volley.newRequestQueue(this);
        JSONObject jsonParams = new JSONObject();
        jsonParams.put("username", registerUsernameField.getText());
        jsonParams.put("password", registerPasswordField.getText());
        jsonParams.put("age", registerAgeField.getText());
        jsonParams.put("firstName", registerFirstNameField.getText());
        jsonParams.put("lastName", registerLastNameField.getText());


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,registerApiEndpoint,jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        authResponse = new Gson().fromJson(String.valueOf(response),AuthenticationResponse.class);
                        Intent loginIntent;
                        if (authResponse.getToken() != null){
                            loginIntent = new Intent(LoginScreen.this,MainScreen.class);
                            loginIntent.putExtra("authToken",authResponse.getToken());
                            startActivity(loginIntent);
                            registerPopup.dismiss();
                        }
                        else {
                            Toast.makeText(LoginScreen.this,authResponse.getErrorMessage(),Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error :" + error.toString());
            }
        });
        mRequestQueue.add(request);
    }



    public void login() throws JSONException {
        if (!usernameLoginField.getText().toString().isEmpty()&&!passwordLoginField.getText().toString().isEmpty()){
            mRequestQueue = Volley.newRequestQueue(this);
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("username", usernameLoginField.getText());
            jsonParams.put("password", passwordLoginField.getText());
            loadingScreenPopup = new Dialog(LoginScreen.this);
            loadingScreenPopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
            loadingScreenPopup.setCancelable(false);
            loadingScreenPopup.setContentView(R.layout.loading_screen);
            loadingScreenPopup.show();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,authenticateApiEndpoint,jsonParams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            authResponse = new Gson().fromJson(String.valueOf(response),AuthenticationResponse.class);
                            Intent loginIntent;
                            if (authResponse.getToken() != null){
                                loginIntent = new Intent(LoginScreen.this,MainScreen.class);
                                loginIntent.putExtra("authToken",authResponse.getToken());
                                startActivity(loginIntent);
                                loadingScreenPopup.dismiss();
                            }
                            else {
                                Toast.makeText(LoginScreen.this,"Error",Toast.LENGTH_LONG).show();
                                loadingScreenPopup.dismiss();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i(TAG, "Error :" + error.toString());
                }
            });
            mRequestQueue.add(request);
        }
        else {
            Toast.makeText(LoginScreen.this,"Error",Toast.LENGTH_LONG).show();
        }


    }


}