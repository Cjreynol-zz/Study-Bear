package com.studybear.cdj.myapplication;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class ForgotPassword extends ActionBarActivity {
    NetworkController networkRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        networkRequest = NetworkController.getInstance(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forgot_password, menu);
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

    public void submitEmail(View v){

        TextView fpEmail = (TextView) findViewById(R.id.fpEmail);
        final String email = fpEmail.getText().toString().trim();

        if(email.isEmpty()) {
            Toast.makeText(getBaseContext(), "Please enter an email address", Toast.LENGTH_LONG).show();
        }
        else {
            String url = getResources().getString(R.string.server_address) + "?rtype=checkEmail";
            StringRequest emailSubmit = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Testing to see if login passed or failed. If passed, the Server returns the string success/failed returns error
                            //Log.d("Response", response);
                            if (response.trim().equals("error"))
                                Toast.makeText(getBaseContext(), "Please enter a registered email address", Toast.LENGTH_LONG).show();
                            else {
                                Intent intent = new Intent(ForgotPassword.this, EmailSubmit.class);
                                startActivity(intent);
                                Toast.makeText(getBaseContext(), "Password request submitted", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getBaseContext(), "Server Error", Toast.LENGTH_LONG).show();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", email);
                    return params;
                }
            };
            //Toast.makeText(getBaseContext(), "Sent request to server", Toast.LENGTH_LONG).show();
            networkRequest.addToRequestQueue(emailSubmit);
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}