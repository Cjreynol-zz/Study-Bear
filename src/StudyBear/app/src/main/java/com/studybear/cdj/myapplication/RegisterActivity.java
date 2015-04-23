package com.studybear.cdj.myapplication;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends ActionBarActivity {
    NetworkController networkRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        networkRequest = NetworkController.getInstance(getApplicationContext());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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

    public void Register(View v){
        TextView fnameview = (TextView) findViewById(R.id.firstname);
        TextView lnameview = (TextView) findViewById(R.id.lastname);
        TextView unameview = (TextView) findViewById(R.id.username);
        TextView emailview = (TextView) findViewById(R.id.email);
        TextView pwordview = (TextView) findViewById(R.id.password);
        TextView confirm = (TextView) findViewById(R.id.confirmpassword);

        final String fname = fnameview.getText().toString().trim();
        final String lname = lnameview.getText().toString().trim();
        final String uname = unameview.getText().toString().trim().toLowerCase();
        final String email = emailview.getText().toString().trim();
        final String pword = pwordview.getText().toString();
        final String pconfirm = confirm.getText().toString();

        if(fname.contains(" ") || lname.contains(" ") || uname.contains(" ") || email.contains(" "))
            Toast.makeText(getBaseContext(),"User fields cannot contain spaces.", Toast.LENGTH_LONG).show();

        else if(fname.isEmpty() || lname.isEmpty() || uname.isEmpty() || email.isEmpty())
            Toast.makeText(getBaseContext(),"All user fields must be filled out.", Toast.LENGTH_LONG).show();

        else if(!email.contains("@") || !email.contains("edu") || !email.contains("."))
            Toast.makeText(getBaseContext(),"Invalid email format.", Toast.LENGTH_LONG).show();

        else if(!pword.equals(pconfirm)) {
            Toast.makeText(getBaseContext(),"Password fields do not match", Toast.LENGTH_LONG).show();
            pwordview.setText(null);
            confirm.setText(null);
        }

        else if(pword.length() < 8){
            Toast.makeText(getBaseContext(),"Passwords must be at least 8 characters.", Toast.LENGTH_LONG).show();
            pwordview.setText(null);
            confirm.setText(null);
        }
        else {

        String url = getResources().getString(R.string.server_address)+ "?rtype=register";
        StringRequest registerPost = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Testing to see if login passed or failed. If passed, the Server returns the string success/failed returns error
                        //Log.d("Response", response);
                        if(response.trim().equals("success"))
                            Toast.makeText(getBaseContext(),"Registration Success",Toast.LENGTH_LONG).show();
                        else if (response.trim().equals("uname_error"))
                            Toast.makeText(getBaseContext(), "Username Already Taken!", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getBaseContext(), "Registration Error!", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(),"Server Error",Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("fname", fname);
                params.put("lname", lname);
                params.put("uname", uname);
                params.put("email", email);
                params.put("pword", pword);
                params.put("pconfirm", pconfirm);
                return params;
            }
        } ;
            //Toast.makeText(getBaseContext(), "Sent request to server", Toast.LENGTH_LONG).show();
            networkRequest.addToRequestQueue(registerPost);
        }
    }
}
