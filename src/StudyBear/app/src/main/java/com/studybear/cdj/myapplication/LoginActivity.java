package com.studybear.cdj.myapplication;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends ActionBarActivity {
    private  NetworkController networkController;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Creating NetworkController to handle Http/Network Request made by the application
        //NetworkController follows the singleton design pattern
        networkController = NetworkController.getInstance(this.getApplicationContext());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void Login(View v){
        //Creating reference to login fields to be checked
        TextView v1 = (TextView) findViewById(R.id.username);
        TextView v2 = (TextView) findViewById(R.id.password);

        final String username = v1.getText().toString().trim().toLowerCase();
        final String password = v2.getText().toString();

        //Constructing URL to be requested
        url = "http://192.168.43.138/index.php?rtype=login";

        //Creating Volley String Request which is a class that constructs a simple Http request that will receive a response back
        //in the form of a string

        StringRequest loginRequest = new StringRequest( Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Testing to see if login passed or failed. If passed, the Server returns the string success/failed returns error
                        Log.d("Response", response);
                        if(response.trim().equals("success")) {
                            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                            intent.putExtra("username",username);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getBaseContext(),"Check username and/or password",Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(),"Cannot communicate with Server.",Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        //Add request to RequestQueue which contains a cache and an abstracted Http Client to send the server request
           networkController.addToRequestQueue(loginRequest);

        }

    public void Register(View v){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }




}

