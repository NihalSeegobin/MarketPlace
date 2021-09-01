package com.example.marketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private EditText userName;
    private EditText userPassword;
    private Button loginBtn;
    private static Context context;
    private static String status,statusMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;





        TextView txtview= findViewById(R.id.signupbtn);
        String text = "Don't have an account? Sign Up here";
        SpannableString ss = new SpannableString(text);
        ForegroundColorSpan fs = new ForegroundColorSpan(Color.YELLOW);

        ClickableSpan cs = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Toast.makeText(MainActivity.this, "Sign Up", Toast.LENGTH_SHORT).show();
            }
        };

        ss.setSpan(fs,22,35,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(cs,22,35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        txtview.setText(ss);
        txtview.setMovementMethod(LinkMovementMethod.getInstance());

        loginBtn = findViewById(R.id.login_btn);
        userName = findViewById(R.id.etUsername);
        userPassword = findViewById(R.id.etPassword);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userNameinput = userName.getText().toString().trim();
                String userPasswordInput = userPassword.getText().toString().trim();

                if(userNameinput.isEmpty() || userPasswordInput.isEmpty()){
                    Toast.makeText(MainActivity.this,"Incomplete fields",Toast.LENGTH_SHORT).show();
                }
                else{

                    ServerLogin(context,userNameinput,userPasswordInput);
                }
            }
        });




    }

    private static void ServerLogin(final Context c, final String email, String password)
    {
        ContentValues cv = new ContentValues();
        cv.put("email",email);
        cv.put("password",password);
        //https://lamp.ms.wits.ac.za/home/s2172765/market_place_login.php

        new ServerCommunicator("https://lamp.ms.wits.ac.za/home/s2172765/market_place_app_login.php", cv) {
            @Override
            protected void onPreExecute(){
            };
            @Override
            protected void onPostExecute(String output) {
                try {


                    //Toast.makeText(context,output,Toast.LENGTH_LONG).show();
                    JSONArray users = new JSONArray(output);
                    JSONObject object_one = users.getJSONObject(0);

                    String mstatus = object_one.getString("login_status");
                    statusMsg = object_one.getString("login_message");

                    Toast.makeText(context,statusMsg,Toast.LENGTH_LONG).show();

                    int statusNum = Integer.parseInt(mstatus);

                    if (statusNum == 0){
                        Toast.makeText(context,"Welcome", Toast.LENGTH_LONG).show();
                    }




                }
                catch(JSONException e){

                    e.printStackTrace();
                }
            }
        }.execute();
    }




}