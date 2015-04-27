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


public class MatchActivity extends ActionBarActivity {

    public NetworkController networkRequest;
    public NavigationBarController navBar;
    public String username;

    public TextView matchName;
    public TextView matchUserName;
    public TextView matchBio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        networkRequest = NetworkController.getInstance(getApplicationContext());
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        navBar = new NavigationBarController(this, username);

        String url = getResources().getString(R.string.server_address) + "?rtype=getMatches&username="+username;

        matchName = (TextView) findViewById(R.id.nameTextView);
        matchUserName = (TextView) findViewById(R.id.userNameTextView);
        matchBio = (TextView) findViewById(R.id.bioTextView);

        populateMatches(url);
    }

    private void populateMatches(String url) {
        JsonObjectRequest matchRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {
                try
                {
                    JSONObject matchedUser = json.getJSONArray("userList").getJSONObject(0);
                    matchName.setText(matchedUser.getString("firstName") + " " + matchedUser.getString("lastName"));
                    matchUserName.setText(matchedUser.getString("userName"));
                    matchBio.setText("biography:\n" + matchedUser.getString("biography"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        networkRequest.addToRequestQueue(matchRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_match, menu);
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
}
