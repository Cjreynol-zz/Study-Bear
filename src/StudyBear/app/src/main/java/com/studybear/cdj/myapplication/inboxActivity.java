package com.studybear.cdj.myapplication;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class inboxActivity extends ActionBarActivity {
    public NetworkController networkRequest;
    public String username;
    Map<String, String> dict = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        networkRequest = NetworkController.getInstance(getApplicationContext());
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        String url = "http://192.168.1.11/studybear/?rtype=getMessages&username="+username;

        JsonObjectRequest getMessagesRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {
                try
                {
                TextView messageList = (TextView) findViewById(R.id.textView7);
                JSONArray mList = json.getJSONArray("messageList");
                StringBuilder mListString = new StringBuilder();
                JSONObject mItem;
                String mItemString;
                String printString;

                for(int i = 0; i < mList.length(); i++)
                {
                    mItem = mList.getJSONObject(i);
                    String sUser = mItem.getString("sendingUser");
                    String rUser  = mItem.getString("receivingUser");
                    String dtime = mItem.getString("niceDate");

                    if(sUser.equals(username)) {
                        if (dict.containsValue(rUser) != true) {
                            dict.put(mItem.getString("msgId"), rUser);
                            mItemString = rUser + "  " + dtime;
                            mListString.append(mItemString + "\n");
                        }
                    }
                    if(rUser.equals(username)) {
                        if (dict.containsValue(sUser) != true) {
                            dict.put(mItem.getString("msgId"), sUser);
                            mItemString = sUser + "  " + dtime;
                            mListString.append(mItemString + "\n");
                        }
                    }

                }
                    messageList.setText(mListString);


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

    public void Logout(View v)
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed(){

    }

    public void inboxActivity (View v)
    {
        Intent intent = new Intent(this, inboxActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    public void NewMessage (View v){
        Intent intent = new Intent(this, NewMessage.class);
        startActivity(intent);
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
}
