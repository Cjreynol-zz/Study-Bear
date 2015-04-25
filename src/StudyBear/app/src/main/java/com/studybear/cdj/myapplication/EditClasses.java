package com.studybear.cdj.myapplication;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class EditClasses extends ActionBarActivity {
    private  NetworkController networkController;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_classes);
        networkController = NetworkController.getInstance(getApplicationContext());

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        String url = getResources().getString(R.string.server_address) + "?rtype=editClasses&username="+username;

        JsonObjectRequest universityListRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {
                Toast.makeText(getApplicationContext(), json.toString(), Toast.LENGTH_LONG).show();
                try {
                    Spinner universitySpinner = (Spinner) findViewById(R.id.spinner2);
                    ArrayList<String> array = new ArrayList<>();
                    JSONArray jsonArray = json.getJSONArray("List");


                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject =  jsonArray.getJSONObject(i);
                            array.add(jsonObject.getString("universityName"));
                        }
                    final ArrayAdapter<String> universityAdapater = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, array);
                    universitySpinner.setAdapter(universityAdapater);

                    universitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            getClasses(universityAdapater.getItem(position));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), volleyError.toString(), Toast.LENGTH_LONG).show();
            }
        });
        networkController.addToRequestQueue(universityListRequest);
        }

    public void getClasses(String university){

        JSONArray classList = new JSONArray();

        String url = getResources().getString(R.string.server_address) + "?rtype=getClasses&username="+username +"&university="+university.trim();
        JsonObjectRequest classListRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {
                Toast.makeText(getApplicationContext(), json.toString(), Toast.LENGTH_LONG).show();
                try {
                    Spinner classSpinner = (Spinner) findViewById(R.id.spinner3);
                    ArrayList<String> array = new ArrayList<>();
                    JSONArray jsonArray = json.getJSONArray("classList");

                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject classItem =  jsonArray.getJSONObject(i);
                        String classItemString = classItem.getString("classId") + ", " + classItem.getString("className") + ", " + classItem.getString("professorLname") + ", " + classItem.getString("professorFname");
                        array.add(classItemString);
                    }
                    final ArrayAdapter<String> classAdapater = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, array);
                    classSpinner.setAdapter(classAdapater);

                    classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            TextView tv = new TextView(getApplicationContext());
                            tv.setText(classAdapater.getItem(position));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), volleyError.toString(), Toast.LENGTH_LONG).show();
            }
        });
        networkController.addToRequestQueue(classListRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_classes, menu);
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
