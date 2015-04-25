package com.studybear.cdj.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class inboxActivity extends ActionBarActivity {
    public NetworkController networkRequest;
    public String username;
    public ArrayList<String> buddies = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        networkRequest = NetworkController.getInstance(getApplicationContext());
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        final LinearLayout inboxLayout  = (LinearLayout) findViewById(R.id.inboxLayout);
        String url = getResources().getString(R.string.server_address) + "?rtype=getMessages&username="+username;

        JsonObjectRequest getMessagesRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {
                try
                {
                    JSONArray messageList = json.getJSONArray("messageList");
                    StringBuilder mListString = new StringBuilder();
                    JSONObject messageItem;
                    String messageString = "";

                    for(int i = 0; i < messageList.length(); i++)
                    {
                        messageItem = messageList.getJSONObject(i);
                        final String sUser = messageItem.getString("sendingUser");
                        final String rUser  = messageItem.getString("receivingUser");
                        String dtime = messageItem.getString("niceDate");

                    if(sUser.equals(username)) {
                        if (!buddies.contains(rUser)) {
                            buddies.add(rUser);
                            messageString = rUser + "  " + dtime;
                            TextView tv = new TextView(getApplicationContext());
                            tv.setText(messageString);
                            tv.setTextColor(Color.parseColor("#315172"));
                            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            llp.setMargins(0, 0, 0, 20); // llp.setMargins(left, top, right, bottom);
                            tv.setLayoutParams(llp);
                            tv.setClickable(true);
                            tv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getApplicationContext(), ConvoActivity.class);
                                    intent.putExtra("buddy", rUser);
                                    intent.putExtra("username", username);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            inboxLayout.addView(tv);
                        }
                    }
                    if(rUser.equals(username)) {
                        if (!buddies.contains(sUser)) {
                            buddies.add(sUser);
                            messageString = sUser + "  " + dtime;
                            TextView tv = new TextView(getApplicationContext());
                            tv.setText(messageString);
                            tv.setTextColor(Color.parseColor("#315172"));
                            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            llp.setMargins(0, 0, 0, 20); // llp.setMargins(left, top, right, bottom);
                            tv.setLayoutParams(llp);
                            tv.setClickable(true);
                            tv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getApplicationContext(), ConvoActivity.class);
                                    intent.putExtra("buddy", rUser);
                                    intent.putExtra("username", username);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            inboxLayout.addView(tv);
                        }
                    }
                }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
        networkRequest.addToRequestQueue(getMessagesRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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

    public void Logout(View v)
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void inboxActivity (View v)
    {
        Intent intent = new Intent(this, inboxActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }
    public void NewMessage (View v){
        Intent intent = new Intent(this, NewMessage.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }
}
