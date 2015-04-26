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
    private ArrayList<String> universityList;
    private String classes;
    private ArrayAdapter<String> universityListAdapater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        networkRequest = NetworkController.getInstance(getApplicationContext());
        Intent getUserInfo = getIntent();
        username = getUserInfo.getStringExtra("username");
        classList = new JSONArray();
        universityList = new ArrayList<>();

        fnameView = (EditText) findViewById(R.id.Fname);
        lnameView = (EditText) findViewById(R.id.Lname);
        biographyView = (EditText) findViewById(R.id.Biography);

        String url = getResources().getString(R.string.server_address) + "?rtype=getProfile&username=" + username;
        JsonObjectRequest profileAttr = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {
                try
                {
                    biographyView.setText(json.getString("biography"));
                    fnameView.setText(json.getString("firstName"));
                    lnameView.setText(json.getString("lastName"));

                    JSONArray classList = json.getJSONArray("classList");
                    StringBuilder classListString = new StringBuilder();
                    JSONObject classItem;
                    String classItemString;

                    for(int i = 0; i < classList.length(); i++)
                    {
                        classItem = classList.getJSONObject(i);
                        classItemString = classItem.getString("classId") + ", " + classItem.getString("className") + ", " + classItem.getString("professorLname") + ", " + classItem.getString("professorFname");

                        if(i + 1 == classList.length())
                            classListString.append(classItemString);
                        else
                            classListString.append(classItemString + "\n");
                    }
                    classes = classListString.toString();
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

        String url2 = getResources().getString(R.string.server_address) + "?rtype=getUniversity&username="+username;
        JsonObjectRequest universityListRequest = new JsonObjectRequest(Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {
                try {
                    Spinner universitySpinner = (Spinner) findViewById(R.id.spinner);

                    JSONArray jsonArray = json.getJSONArray("List");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        universityList.add(jsonObject.getString("universityName"));
                    }
                    universityListAdapater = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, universityList);
                    universitySpinner.setAdapter(universityListAdapater);
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
        networkRequest.addToRequestQueue(universityListRequest);
    }

    public void Submit(View v){
        Spinner universitySpinner = (Spinner) findViewById(R.id.spinner);
        university = universityListAdapater.getItem(universitySpinner.getSelectedItemPosition());

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

                return params;
            }
        };
        networkRequest.addToRequestQueue(postRequest);
    }

    public void editClasses(View v){
        Intent intent = new Intent(this, EditClasses.class);
        intent.putExtra("username", username);
        intent.putExtra("classes", classes);
        intent.putExtra("university", university);
        startActivity(intent);
        finish();
    }

    public void Back(View v){
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("username",username);
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
