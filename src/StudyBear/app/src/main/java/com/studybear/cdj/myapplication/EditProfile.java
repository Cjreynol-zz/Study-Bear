package com.studybear.cdj.myapplication;

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
    private String fname;
    private String lname;
    private String university;
    private String biography;
    private EditText fnameView;
    private EditText lnameView;
    private Spinner universityView;
    private EditText biographyView;
    public static JSONArray classList;
    private static final String TAG = "EditProfile";
    public static ArrayList<String> array;
    private String classes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        networkRequest = NetworkController.getInstance(getApplicationContext());
        Intent getUserInfo = getIntent();
        username = getUserInfo.getStringExtra("username");
        fname = getUserInfo.getStringExtra("fname");
        lname = getUserInfo.getStringExtra("lname");
        biography = getUserInfo.getStringExtra("bio");
        classes = getUserInfo.getStringExtra("classes");
        classList = new JSONArray();
        //classList.put(" ");

        fnameView = (EditText) findViewById(R.id.Fname);
        lnameView = (EditText) findViewById(R.id.Lname);
        universityView = (Spinner) findViewById(R.id.spinner);
        biographyView = (EditText) findViewById(R.id.Biography);
        TextView tv = new TextView(this);
        tv.setText(classes);
        LinearLayout ly = (LinearLayout) findViewById(R.id.layoutD);
        ly.addView(tv);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        R.array.universitys, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        universityView.setAdapter(adapter);

        fnameView.setText(fname);
        lnameView.setText(lname);
        biographyView.setText(biography);

    }

    public void Submit(View v){
        university = "Georgia Regents University";

        String url = "http://192.168.17.1/?rtype=editProfile";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(s.trim().equals("success"))
                Toast.makeText(getBaseContext(), "Profile Updated Successfully.", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getBaseContext(), s, Toast.LENGTH_LONG).show();
                Log.d(TAG, s);
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
