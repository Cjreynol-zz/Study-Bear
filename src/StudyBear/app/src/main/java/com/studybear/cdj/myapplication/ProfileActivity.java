package com.studybear.cdj.myapplication;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

        ImageButton matchButton  = (ImageButton) findViewById(R.id.matchButton);
        matchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MatchActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
                finish();
            }
        });

        ImageButton messageButton  = (ImageButton) findViewById(R.id.messageButton);
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), inboxActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
                finish();
            }
        });

        ImageButton classButton  = (ImageButton) findViewById(R.id.classButton);
        classButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditClasses.class);
                intent.putExtra("username",username);
                startActivity(intent);
                finish();
            }
        });

        ImageButton profileButton  = (ImageButton) findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
                finish();
            }
        });

        networkRequest = NetworkController.getInstance(getApplicationContext());
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        String url = getResources().getString(R.string.server_address) + "?rtype=getProfile&username=" + username;

        bio = (TextView) findViewById(R.id.Biography);
        classes = (TextView) findViewById(R.id.Classes);
        name = (TextView) findViewById(R.id.Name);
        university = (TextView) findViewById(R.id.University);

        JsonObjectRequest profileAttr = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {
                try
                {
                    Log.d("JSONRESPONSE", json.toString());
                    bio.setText(json.getString("biography"));
                    name.setText(json.getString("firstName") + " " + json.getString("lastName"));
                    university.setText(json.getString("universityName"));

                    if(!json.isNull("classList")) {
                        JSONArray classList = json.getJSONArray("classList");
                        StringBuilder classListString = new StringBuilder();
                        JSONObject classItem;
                        String classItemString;

                        for (int i = 0; i < classList.length(); i++) {
                            classItem = classList.getJSONObject(i);
                            classItemString = classItem.getString("classId") + ", " + classItem.getString("className") + ", " + classItem.getString("professorLname") + ", " + classItem.getString("professorFname");

                            if (i + 1 == classList.length())
                                classListString.append(classItemString);
                            else
                                classListString.append(classItemString + "\n\n");
                        }
                        classes.setText(classListString.toString());
                    }
                    else
                        classes.setText("No Classes");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),volleyError.toString(),Toast.LENGTH_LONG).show();
            }
        });
        networkRequest.addToRequestQueue(profileAttr);
    }

    public void EditProfile(View v)
    {
        Intent intent = new Intent(this, EditProfile.class);
        String [] nameArray = name.getText().toString().trim().split(" ");
        intent.putExtra("fname", nameArray[0]);
        intent.putExtra("lname", nameArray[1]);
        intent.putExtra("username", username);
        intent.putExtra("bio", bio.getText().toString().trim());
        intent.putExtra("university", university.getText().toString().trim());
        intent.putExtra("classes", classes.getText().toString().trim());
        startActivity(intent);
        finish();
    }

    public void Logout(View v)
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
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

    public void inboxActivity (View v)
    {
        Intent intent = new Intent(this, inboxActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }

    public void findMatch (View v) {
        Intent intent = new Intent(this, MatchActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }

}
