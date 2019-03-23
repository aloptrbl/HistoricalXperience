package com.application.workshop2.historicalxperience;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.application.workshop2.historicalxperience.app.AppConfig;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RetrieveMTBuilding extends AppCompatActivity {

    ListView listview;
    TextView building_name, building_address;
    private String jsonMTResult;
    SimpleAdapter simpleAdapter;
    final List<Map<String,String>> buildingList = new ArrayList<Map<String,String>>();
    private String MTurl = "https://finerbinder.000webhostapp.com/apps/retrieveMT.php?";

   // private String MTurl = "https://finerbinder.000webhostapp.com/apps/retrieveMTdetails.php?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_mtbuilding);

            listview = (ListView)findViewById(R.id.listViewEarly);
            building_name = (TextView) findViewById(R.id.building_name);
            building_address = (TextView) findViewById(R.id.building_address);
            accessWebService();
        }

        private class JsonReadTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(params[0]);
                httppost.setHeader("Content-type", "application/json");
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    jsonMTResult = inputStreamToString(
                            response.getEntity().getContent()).toString();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }


            private StringBuilder inputStreamToString(InputStream is) {
                String MTLine = "";
                StringBuilder answer = new StringBuilder();
                BufferedReader MTbr = new BufferedReader(new InputStreamReader(is));

                try {
                    while ((MTLine = MTbr.readLine()) != null) {
                        answer.append(MTLine);
                    }
                } catch (IOException e) {
                    // e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error..." + e.toString(), Toast.LENGTH_LONG).show();
                }
                return answer;
            }

            @Override
            protected void onPostExecute(String result) {
                ListDrwaer();
            }

        }

    public void accessWebService() {

        RetrieveMTBuilding.JsonReadTask MTask = new RetrieveMTBuilding.JsonReadTask();
        // passes values for the urls string array
        MTask.execute(new String[] {MTurl});
    }

    // build hash set for list view
    public void ListDrwaer() {


        try {
            JSONObject jsonResponse = new JSONObject(jsonMTResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("building");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String build_name = jsonChildNode.optString("building_name");
                String build_id = jsonChildNode.optString("building_id");
                String build_add = jsonChildNode.optString("building_address");

                //String outPut = name + "-" + idR;

                HashMap<String,String> info = new HashMap<String,String>();
                info.put(AppConfig.KEY_BUILDID,build_id);
                info.put(AppConfig.KEY_BUILDNAME,build_name);
                info.put(AppConfig.KEY_BUILDADD,build_add);
                buildingList.add(info);
            }
        } catch (JSONException e) {

            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
        }

        simpleAdapter = new SimpleAdapter(getApplicationContext(), buildingList,R.layout.buildinglistinfo, new String[] {AppConfig.KEY_BUILDID,AppConfig.KEY_BUILDNAME,AppConfig.KEY_BUILDADD}, new int[] {R.id.building_id,R.id.building_name,R.id.building_address});
        listview.setAdapter(simpleAdapter);



        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intentBuildingDetailsActivity = new Intent(RetrieveMTBuilding.this,BuildingDetailsActivity.class);
                TextView building_id = (TextView)findViewById(R.id.building_id);
                intentBuildingDetailsActivity.putExtra("idDetail",id);
                startActivityForResult(intentBuildingDetailsActivity,10001);
            }
        });

    }
    }
