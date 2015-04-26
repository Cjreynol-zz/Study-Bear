package com.studybear.cdj.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Response.*;


public class EditProfile extends ActionBarActivity {
    private NetworkController networkRequest;
    private String username;
    private String university;
    private EditText fnameView;
    private EditText lnameView;
    private EditText biographyView;
    public static JSONArray classList;
    private static final String TAG = "EditProfile";
    public String classes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        networkRequest = NetworkController.getInstance(getApplicationContext());
        Intent getUserInfo = getIntent();

        username = getUserInfo.getStringExtra("username");
        String fname = getUserInfo.getStringExtra("fname");
        String lname = getUserInfo.getStringExtra("lname");
        String biography = getUserInfo.getStringExtra("bio");
        classes = getUserInfo.getStringExtra("classes");
        classList = new JSONArray();

        fnameView = (EditText) findViewById(R.id.Fname);
        lnameView = (EditText) findViewById(R.id.Lname);
        biographyView = (EditText) findViewById(R.id.Biography);

        fnameView.setText(fname);
        lnameView.setText(lname);
        biographyView.setText(biography);

        /*
        final LinearLayout ly = (LinearLayout) findViewById(R.id.layoutD);
        String [] classParse = classes.split("\n");
        for (int i = 0; i < classParse.length; i++)
        {
            final TextView tv = new TextView(this);
            tv.setText(classParse[i]);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    classList.put(tv.getText().toString());
                    Toast.makeText(getApplicationContext(), tv.getText().toString(), Toast.LENGTH_LONG).show();
                    ly.removeView(tv);
                }
            });
            ly.addView(tv);
        }
        */
    }

    public void Submit(View v){
        university = "Georgia Regents University";

        String url = getResources().getString(R.string.server_address) + "?rtype=editProfile";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(s.trim().equals("success"))
                Toast.makeText(getBaseContext(), "Profile Updated.", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getBaseContext(), s, Toast.LENGTH_LONG).show();
                Log.d(TAG, s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(),"Server Error"+error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("fname", fnameView.getText().toString().trim());
                params.put("lname", lnameView.getText().toString().trim());
                params.put("university", university);
                params.put("biography", biographyView.getText().toString().trim());
                params.put("uname", username);
                params.put("classList", classList.toString());

                return params;
            }
        };
        networkRequest.addToRequestQueue(postRequest);
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("username",username);
        startActivity(intent);
        finish();
    }

    public void editClasses(View v){
        Intent intent = new Intent(this, EditClasses.class);
        intent.putExtra("username", username);
        intent.putExtra("classes", classes);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
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
