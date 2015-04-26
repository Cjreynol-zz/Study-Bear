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

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class EditClasses extends ActionBarActivity {
    private  NetworkController networkController;
    private String username;
    private String TAG = "EditClasses";
    private ArrayAdapter<String> classAdapater;
    private ArrayList<String> adapterClassList;
    private ArrayList<String> removeList;
    private ArrayList<String> insertList;
    private ArrayList<String> originalClassList;
    private ArrayAdapter<String> insertListAdapter;
    private ArrayList<String> addList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_classes);
        networkController = NetworkController.getInstance(getApplicationContext());

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        adapterClassList = new ArrayList<>();
        removeList = new ArrayList<>();
        addList = new ArrayList<>();
        insertList = new ArrayList<>();
        originalClassList = new ArrayList<>();

        String url = getResources().getString(R.string.server_address) + "?rtype=getUserClasses&username="+username;
        JsonObjectRequest classListRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {
                try {
                    Log.d(TAG, json.toString());
                    JSONArray jsonArray = json.getJSONArray("classList");
                    JSONObject classItem;
                    for (int i = 0; i < jsonArray.length(); i++){

                        classItem =  jsonArray.getJSONObject(i);
                        String classItemString = classItem.getString("classId") + ", " + classItem.getString("className") + ", " + classItem.getString("professorLname") + ", " + classItem.getString("professorFname");
                        originalClassList.add(classItemString);
                        addList.add(classItemString);
                    }
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

        insertListAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1, addList);
        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(insertListAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Spinner classSpinner =  (Spinner) findViewById(R.id.spinner3);
                if(originalClassList.contains(classAdapater.getItem(classSpinner.getSelectedItemPosition())))
                    removeList.add(insertListAdapter.getItem(position));
                insertListAdapter.remove(insertListAdapter.getItem(position));
                insertListAdapter.notifyDataSetChanged();
            }
        });

        String url2 = getResources().getString(R.string.server_address) + "?rtype=getUniversity&username="+username;
        JsonObjectRequest universityListRequest = new JsonObjectRequest(Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {
                try {
                    Spinner universitySpinner = (Spinner) findViewById(R.id.spinner2);
                    adapterClassList.clear();
                    JSONArray jsonArray = json.getJSONArray("List");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        adapterClassList.add(jsonObject.getString("universityName"));
                    }

                    final ArrayAdapter<String> universityAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, adapterClassList);
                    universitySpinner.setAdapter(universityAdapter);
                    universitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                getClasses(universityAdapter.getItem(position));
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
        Log.d(TAG, url);
        JsonObjectRequest classListRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {
                Log.d(TAG, json.toString());
                try {
                    final Spinner classSpinner = (Spinner) findViewById(R.id.spinner3);
                    classSpinner.setAdapter(null);

                    ArrayList<String> array = new ArrayList<>();

                    JSONArray jsonArray = json.getJSONArray("classList");
                    JSONObject classItem;
                    Log.d(TAG, json.toString());
                    for (int i = 0; i < jsonArray.length(); i++){

                        classItem =  jsonArray.getJSONObject(i);
                        String classItemString = classItem.getString("classId") + ", " + classItem.getString("className") + ", " + classItem.getString("professorLname") + ", " + classItem.getString("professorFname");
                        array.add(classItemString);
                    }
                    classAdapater = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, array);
                    classSpinner.setAdapter(classAdapater);
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG,volleyError.toString());
                Toast.makeText(getApplicationContext(),volleyError.toString(),Toast.LENGTH_LONG).show();
            }
        });
        networkController.addToRequestQueue(classListRequest);
    }

    public void Add(View v){
        Spinner classSpinner =  (Spinner) findViewById(R.id.spinner3);
        ListView lv = (ListView) findViewById(R.id.listView);

        if(!addList.contains(classAdapater.getItem(classSpinner.getSelectedItemPosition()))) {
            insertListAdapter.add(classAdapater.getItem(classSpinner.getSelectedItemPosition()));
            insertListAdapter.notifyDataSetChanged();
        }

        if(!originalClassList.contains(classAdapater.getItem(classSpinner.getSelectedItemPosition())) && !insertList.contains(classAdapater.getItem(classSpinner.getSelectedItemPosition())))
            insertList.add(classAdapater.getItem(classSpinner.getSelectedItemPosition()));
        Log.d(TAG, insertList.toString());
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Spinner classSpinner =  (Spinner) findViewById(R.id.spinner3);
                if(originalClassList.contains(classAdapater.getItem(classSpinner.getSelectedItemPosition())))
                    removeList.add(insertListAdapter.getItem(position));
                insertListAdapter.remove(insertListAdapter.getItem(position));
                insertListAdapter.notifyDataSetChanged();
            }
        });
        lv.setAdapter(insertListAdapter);
    }

    public void Save(View v){

        final JSONArray jsonRemoveList = new JSONArray();
        for(int i = 0; i < removeList.size();i++){
            jsonRemoveList.put(removeList.get(i));
        }

        final JSONArray jsonInsertList = new JSONArray();
        for(int i = 0; i < insertList.size();i++){
            jsonInsertList.put(insertList.get(i));
        }

        String url = getResources().getString(R.string.server_address) + "?rtype=saveClasses&username";
        StringRequest saveRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                    Toast.makeText(getBaseContext(), "Classes Updated.", Toast.LENGTH_LONG).show();
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
                params.put("username", username);
                params.put("removeList", jsonRemoveList.toString());
                params.put("insertList", jsonInsertList.toString());

                return params;
            }
        };
        networkController.addToRequestQueue(saveRequest);
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("username",username);
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
