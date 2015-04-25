package com.studybear.cdj.myapplication;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


public class EditClasses extends ActionBarActivity {
    private  NetworkController networkController;
    private String username;
    private String TAG = "EditClasses";
    private ArrayAdapter<String> classAdapater;
    private JSONArray classList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_classes);
        networkController = NetworkController.getInstance(getApplicationContext());
        classList = new JSONArray();

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        String url = getResources().getString(R.string.server_address) + "?rtype=editClasses&username="+username;

        JsonObjectRequest universityListRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {
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
                            //Toast.makeText(getApplicationContext(), universityAdapater.getItem(position), Toast.LENGTH_LONG).show();
                            try {
                                getClasses(universityAdapater.getItem(position));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
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

    public void getClasses(String university) throws UnsupportedEncodingException {

        String encodedParam = URLEncoder.encode(university,"UTF-8");
        String url = getResources().getString(R.string.server_address) + "?rtype=getClasses&username="+username +"&university=" + encodedParam;
        //Toast.makeText(getApplicationContext(),url,Toast.LENGTH_LONG).show();
        Log.d(TAG, url);
        JsonObjectRequest classListRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {
                //Toast.makeText(getApplicationContext(),json.toString(),Toast.LENGTH_LONG).show();
                try {
                    final Spinner classSpinner = (Spinner) findViewById(R.id.spinner3);
                    classSpinner.setAdapter(null);

                    ArrayList<String> array = new ArrayList<>();
                    JSONArray jsonArray = json.getJSONArray("classes");
                    JSONObject classItem;
                    Log.d(TAG, json.toString());
                    for (int i = 0; i < jsonArray.length(); i++){

                        classItem =  jsonArray.getJSONObject(i);
                        String classItemString = classItem.getString("0") + ", " + classItem.getString("className") + ", " + classItem.getString("professorLname") + ", " + classItem.getString("professorFname");
                        array.add(classItemString);
                    }
                    classAdapater = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, array);
                    classSpinner.setAdapter(classAdapater);
                    classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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
                Toast.makeText(getApplicationContext(),volleyError.toString(),Toast.LENGTH_LONG).show();
            }
        });
        networkController.addToRequestQueue(classListRequest);
    }

    public void Add(View v){
        Spinner classSpinner =  (Spinner) findViewById(R.id.spinner3);
        TextView tv = new TextView(getApplicationContext());
        tv.setText(classAdapater.getItem(classSpinner.getSelectedItemPosition()));
        classList.put(classAdapater.getItem(classSpinner.getSelectedItemPosition()));
        LinearLayout ly = (LinearLayout) findViewById(R.id.LinearLayoutC);
        ly.addView(tv);
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
