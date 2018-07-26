package com.coderfolk.numberfacts;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private Button button;
    private LinearLayout linearLayout,layout;
    private String trivia;
    private String math;
    private String date;
    private ProgressBar progressBar;
    private static String url = "http://numbersapi.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.number);
        button = (Button) findViewById(R.id.button);
        final TextView t1, t2, t3;

        linearLayout=(LinearLayout) findViewById(R.id.linear);
        layout=(LinearLayout) findViewById(R.id.layout);
        progressBar =(ProgressBar) findViewById(R.id.progress);
        t1 = (TextView) findViewById(R.id.factmath);
        t2 = (TextView) findViewById(R.id.facttrivia);
        t3 = (TextView) findViewById(R.id.factdate);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                hideKeypad();
                if (isOnline()) {
                    String value=editText.getText().toString();
                    fetch(url + value + "/trivia",t1);
                    fetch(url + value + "/math",t2);
                    if(Integer.parseInt(value)>0&&Integer.parseInt(value)<32){
                        fetch(url + value + "/date",t3);
                    }
                    else{
                        t3.setText("Date fact: Not available");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Connect to internet", Toast.LENGTH_SHORT).show();
                }
                linearLayout.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.FadeInUp)
                        .duration(700)
                        .repeat(0)
                        .playOn(linearLayout);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public boolean isOnline() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    void fetch(String url,final TextView t) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                t.setText((CharSequence)response);
                Log.d("result 0", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error occured", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }
    private void hideKeypad() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}
