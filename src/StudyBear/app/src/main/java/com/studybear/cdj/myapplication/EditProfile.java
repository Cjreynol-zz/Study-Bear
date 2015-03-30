package com.studybear.cdj.myapplication;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Response.*;


public class EditProfile extends ActionBarActivity {
    private NetworkController networkRequest;
    private String username;
    private String fname;
    private String lname;
    //String email;
    private String university;
    private String biography;
    private EditText fnameView;
    private EditText lnameView;
    //EditText emailView;
    private Spinner universityView;
    private EditText biographyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent getUser = getIntent();
        username = getUser.getStringExtra("username");
        networkRequest = NetworkController.getInstance(getApplicationContext());
        String url = "http://192.168.43.138/?rtype=getProfile&username="+username;

        fnameView = (EditText) findViewById(R.id.Fname);
        lnameView = (EditText) findViewById(R.id.Lname);
        //emailView = (EditText) findViewById(R.id.Email);
        universityView = (Spinner) findViewById(R.id.spinner);
        biographyView = (EditText) findViewById(R.id.Biography);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        R.array.universitys, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        JsonObjectRequest getProfile = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {
                try
                {
                    JSONObject populate = json;
                    biographyView.setText(populate.getString("biography"));
                    fnameView.setText(populate.getString("firstName"));
                    lnameView.setText(populate.getString("lastName"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        networkRequest.addToRequestQueue(getProfile);

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

    public void Submit(View v){

        fname = fnameView.getText().toString().trim();
        lname = lnameView.getText().toString().trim();
        //email = emailView.getText().toString().trim();
        //university = universityView.getSelectedItem().toString().trim();
        university = "Georgia Regents University";
        biography = biographyView.getText().toString().trim();


        String url = "http://192.168.43.138/?rtype=editProfile";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Toast.makeText(getBaseContext(), "Profile Updated Successfully.", Toast.LENGTH_LONG).show();
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
                params.put("fname", fname);
                params.put("lname", lname);
                //params.put("email", email);
                params.put("university", university);
                params.put("biography", biography);
                params.put("uname", username);

                return params;
            }
        };
        networkRequest.addToRequestQueue(postRequest);
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("username",username);
        startActivity(intent);

    }
}
