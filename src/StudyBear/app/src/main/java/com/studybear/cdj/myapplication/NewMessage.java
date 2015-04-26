package com.studybear.cdj.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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


public class NewMessage extends ActionBarActivity {
    public NetworkController networkRequest;
    public TextView messageTo;
    public TextView messageBody;
    public String username;
    public String fillTo;
    public Boolean error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        networkRequest = NetworkController.getInstance(getApplicationContext());
        messageTo = (TextView) findViewById(R.id.messageTo);
        messageBody = (TextView) findViewById(R.id.messageBody);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        fillTo = intent.getStringExtra("fillTo");
        if (fillTo != null) {
            messageTo.setText(fillTo, TextView.BufferType.EDITABLE);
            messageBody.requestFocus();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_message, menu);
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

    public void SendMessage(View v) {
        error = false;
        final String mTo = messageTo.getText().toString().trim();
        final String mBody = messageBody.getText().toString().trim();

        String url = getResources().getString(R.string.server_address)+ "?rtype=checkTo";
        StringRequest registerPost = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Testing to see if login passed or failed. If passed, the Server returns the string success/failed returns error
                        //Log.d("Response", response);
                        if(response.trim().equals("error")) {
                            Toast.makeText(getBaseContext(), "Please enter valid username", Toast.LENGTH_LONG).show();
                            error = true;
                        }
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
                params.put("mTo",mTo);
                return params;
            }
        };
        //Toast.makeText(getBaseContext(), "Sent request to server", Toast.LENGTH_LONG).show();
        networkRequest.addToRequestQueue(registerPost);
        if(error){
        }
        else if(mTo.equals(username)){
            Toast.makeText(getBaseContext(), "Cannot send message to yourself", Toast.LENGTH_LONG).show();
        }
        else if(mTo.isEmpty()) {
            Toast.makeText(getBaseContext(), "Please enter a recipient", Toast.LENGTH_LONG).show();
        }
        else if (mBody.isEmpty()){
            Toast.makeText(getBaseContext(), "Please enter text in message body", Toast.LENGTH_LONG).show();
        }
        else{
            String url1 = getResources().getString(R.string.server_address) + "?rtype=newMessage";
            StringRequest postRequest = new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    if(s.trim().equals("success"))
                        Toast.makeText(getBaseContext(), "Message sent!", Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getBaseContext(),"Cannot communicate with Server.",Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                protected Map<String, String> getParams(){
                    Map<String, String> params = new HashMap<>();
                    params.put("mTo", mTo);
                    params.put("mBody", mBody);
                    params.put("uName", username);
                    return params;
                }
            };
            if(!error) {
                networkRequest.addToRequestQueue(postRequest);
                Intent intent = new Intent(this, inboxActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                finish();
            }
        }
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, inboxActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }
}