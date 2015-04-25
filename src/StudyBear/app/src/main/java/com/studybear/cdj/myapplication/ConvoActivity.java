package com.studybear.cdj.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ConvoActivity extends ActionBarActivity {
    public NetworkController networkRequest;
    public String buddy;
    public String username;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convo);

        networkRequest = NetworkController.getInstance(getApplicationContext());
        Intent intent = getIntent();
        buddy = intent.getStringExtra("buddy");
        username = intent.getStringExtra("username");
        TextView tvBuddy = (TextView) findViewById(R.id.tvBuddy);
        tvBuddy.setText(buddy);
        TextView tv = new TextView(this);

        String url = getResources().getString(R.string.server_address) + "?rtype=getConvo&buddy="+buddy;
        final LinearLayout lyc  = (LinearLayout) findViewById(R.id.cLayout);

        JsonObjectRequest getMessagesRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {
                try
                {
                    JSONArray messageList = json.getJSONArray("messageList");
                    StringBuilder mListString = new StringBuilder();
                    JSONObject mItem;
                    String mItemString = "";
                    String printString;

                    for(int i = 0; i < messageList.length(); i++)
                    {
                        mItem = messageList.getJSONObject(i);
                        final String message = mItem.getString("body");
                        final String sUser = mItem.getString("sendingUser");
                        final String rUser  = mItem.getString("receivingUser");

                        mItemString = message;
                        TextView tv = new TextView(getApplicationContext());
                        tv.setText(mItemString);
                        tv.setPadding(30, 30, 30, 30);
                        tv.setTextColor(Color.parseColor("#FFFFFF"));
                        tv.setBackgroundColor(Color.parseColor("#99315172"));
                        tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.sback));
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        tv.setWidth(600);
                        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        llp.setMargins(0, 0, 0, 50); // llp.setMargins(left, top, right, bottom);
                        tv.setLayoutParams(llp);



                        if(rUser.equals(buddy)){
                            llp.gravity = Gravity.RIGHT;
                            tv.setTextColor(Color.parseColor("#315172"));
                            tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.rback));
                            tv.setLayoutParams(llp);
                        }
                        Button cButton = (Button) findViewById(R.id.cButton);
                        cButton.setText("Send " + buddy + " a message");

                        lyc.addView(tv);

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
        final ScrollView scrollview = ((ScrollView) findViewById(R.id.convoScrollView));
        Runnable r = new Runnable() {
            @Override
            public void run() {
                scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                scrollview.setVisibility(View.VISIBLE);
            }
        };
        scrollview.postDelayed(r, 200);
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

    public void NewMessage (View v){
        Intent intent = new Intent(this, NewMessage.class);
        intent.putExtra("username", username);
        intent.putExtra("fillTo", buddy);
        startActivity(intent);
    }

    public void inboxActivity (View v)
    {
        Intent intent = new Intent(this, inboxActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void Logout(View v)
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


}