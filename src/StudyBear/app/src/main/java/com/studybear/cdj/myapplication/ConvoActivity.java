package com.studybear.cdj.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConvoActivity extends ActionBarActivity {
    public NetworkController networkRequest;
    public NavigationBarController navigationBar;
    public String buddy;
    public String username;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convo);

        networkRequest = NetworkController.getInstance(getApplicationContext());
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        navigationBar = new NavigationBarController(this, username);
        buddy = intent.getStringExtra("buddy");
        setTitle(buddy);
        Button convoButton = (Button) findViewById(R.id.convoButton);
        convoButton.setText("Send " + buddy + " a message");

        String url = getResources().getString(R.string.server_address)+"?rtype=getConvo&buddy="+buddy+"&username="+username;
        final LinearLayout convoLayout  = (LinearLayout) findViewById(R.id.convoLayout);

        JsonObjectRequest getMessagesRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {
                try
                {
                    JSONArray messageList = json.getJSONArray("messageList");
                    JSONObject message;

                    for(int i = 0; i < messageList.length(); i++)
                    {
                        message = messageList.getJSONObject(i);
                        final String body = message.getString("body");
                        final String sUser = message.getString("sendingUser");

                        TextView tv = new TextView(getApplicationContext());
                        tv.setText(body);
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

                        if(sUser.equals(username)){
                            llp.gravity = Gravity.RIGHT;
                            tv.setTextColor(Color.parseColor("#315172"));
                            tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.rback));
                            tv.setLayoutParams(llp);
                        }
                        convoLayout.addView(tv);
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
        scrollview.postDelayed(r, 100);
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
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
                        .show();
                break;
            // action with ID action_settings was selected
            case R.id.action_logout:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }

    public void NewMessage (View v){
        Intent intent = new Intent(this, NewMessage.class);
        intent.putExtra("username", username);
        intent.putExtra("fillTo", buddy);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, inboxActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }
}
