package com.studybear.cdj.myapplication;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.*;

import java.io.IOException;


public class ProfileActivity extends ActionBarActivity {
    public NetworkController networkRequest;
    public TextView bio;
    public TextView classes;
    public TextView name;
    public TextView university;
    public String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        networkRequest = NetworkController.getInstance(getApplicationContext());
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        String url = "http://10.8.5.68/studybear/?rtype=getProfile&username="+username;

        bio = (TextView) findViewById(R.id.Biography);
        classes = (TextView) findViewById(R.id.Classes);
        name = (TextView) findViewById(R.id.Name);
        university = (TextView) findViewById(R.id.University);

        JsonObjectRequest profileAttr = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {
                try
                {
                    JSONObject populate = json;
                    bio.setText("Biography:\n" + populate.getString("biography"));
                    name.setText(populate.getString("firstName") + " " + populate.getString("lastName"));
                    university.setText(populate.getString("universityName"));

                    JSONArray classList = populate.getJSONArray("CLASSES");
                    StringBuilder classString = new StringBuilder();
                    for(int i = 0; i < classList.length(); i++)
                    {
                        if(i + 1 == classList.length())
                            classString.append("- " + classList.getString(i));
                        else
                             classString.append("- " + classList.getString(i) + "\n\n");
                    }
                    classes.setText("Classes:\n" + classString.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        networkRequest.addToRequestQueue(profileAttr);
    }
    @Override
    public void onBackPressed(){

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

    public void EditProfile(View v)
    {
        Intent intent = new Intent(this, EditProfile.class);
        intent.putExtra("username", username);
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
