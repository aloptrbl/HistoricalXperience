package com.application.workshop2.historicalxperience;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.application.workshop2.historicalxperience.app.AppConfig;
import com.application.workshop2.historicalxperience.helper.GetDataAdapter;
import com.application.workshop2.historicalxperience.helper.RecyclerViewAdapter;

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

public class RetrieveCTBuilding extends AppCompatActivity implements ItemClickListener{

    private Button btnBack;

    List<GetDataAdapter> GetDataAdapter1;


    RecyclerView recyclerView;

    RecyclerView.LayoutManager recyclerViewlayoutManager;

    RecyclerViewAdapter recyclerViewadapter;

    String recipeWeek;
    String GET_JSON_DATA_HTTP_URL = "https://finerbinder.000webhostapp.com/apps/retrieveMTList.php?area_id=";
    String JSON_IMAGE_TITLE_ID = "building_id";
    String JSON_IMAGE_TITLE_NAME = "building_name";
    String JSON_IMAGE_URL = "url";

    JsonArrayRequest jsonArrayRequest ;

    RequestQueue requestQueue ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_retrieve_ctbuilding);

        btnBack = (Button) findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        UserChooseArea.class);
                startActivity(i);
                finish();
            }
        });

        GetDataAdapter1 = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview1);

        recyclerView.setHasFixedSize(true);

        recyclerViewlayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(recyclerViewlayoutManager);


        int idArea = 4;
        final String ID = String.valueOf(idArea);


        JSON_DATA_WEB_CALL(ID);


    }

    public void JSON_DATA_WEB_CALL(final String ID){

        jsonArrayRequest = new JsonArrayRequest(GET_JSON_DATA_HTTP_URL+ID,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        JSON_PARSE_DATA_AFTER_WEBCALL(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(jsonArrayRequest);
    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array){

        for(int i = 0; i<array.length(); i++) {

            GetDataAdapter GetDataAdapter2 = new GetDataAdapter();

            JSONObject json = null;
            try {

                json = array.getJSONObject(i);

                GetDataAdapter2.setImageTitleNamee(json.getString(JSON_IMAGE_TITLE_NAME));

                GetDataAdapter2.setImageServerUrl(json.getString(JSON_IMAGE_URL));

                GetDataAdapter2.setImageTitleId(json.getString(JSON_IMAGE_TITLE_ID));

            } catch (JSONException e) {

                e.printStackTrace();
            }
            GetDataAdapter1.add(GetDataAdapter2);
        }

        recyclerViewadapter = new RecyclerViewAdapter(GetDataAdapter1, this);

        recyclerView.setAdapter(recyclerViewadapter);

        recyclerViewadapter.setClickListener(this);

    }

    @Override
    public void onClick(View view, int position) {
        Intent intentDetail = new Intent(RetrieveCTBuilding.this, MTDetail.class);
        TextView name = (TextView)view.findViewById(R.id.mtlistID);
        intentDetail.putExtra("ItemPosition",name.getText().toString());
        startActivity(intentDetail);
    }
}
