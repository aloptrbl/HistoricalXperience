package com.application.workshop2.historicalxperience;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.application.workshop2.historicalxperience.app.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class BuildingDetailsActivity extends AppCompatActivity {

    TextView buildingID;
    TextView buildingTitle, BDUrl, buildingHistory;
    ProgressDialog loading;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_details);


            Bundle extras = getIntent().getExtras();
            int latValue = extras.getInt("idDetail");
            //String id = String.valueOf(latValue);
            int lat = latValue + 1;
            buildingID = (TextView)findViewById(R.id.building_id);
            buildingID.setText(String.valueOf(lat));

            imageView = (ImageView)findViewById(R.id.building_image);
            buildingTitle = (TextView)findViewById(R.id.buildingTitle);
            BDUrl = (TextView)findViewById(R.id.BDurl);
            buildingHistory = (TextView)findViewById(R.id.building_history);
            getData();
        }

    public void getData(){
        String idBuilding = buildingID.getText().toString().trim();

        if (buildingTitle.equals("")) {
            Toast.makeText(BuildingDetailsActivity.this, "Please enter an id", Toast.LENGTH_LONG).show();
            return;
        }
        loading = ProgressDialog.show(BuildingDetailsActivity.this,"Please wait...","Fetching...",false,false);

        String url = AppConfig.URL_building+idBuilding;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BuildingDetailsActivity.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(BuildingDetailsActivity.this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response){

        String urlImage = "";
        String bname = "";
        String bhistory = "";

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(AppConfig.JSON_ARRAY);
            JSONObject buildingData = result.getJSONObject(0);
            bname = buildingData.getString(AppConfig.KEY_BUILDNAME);
            bhistory = buildingData.getString(AppConfig.KEY_BUILDHISTORY );
            urlImage = buildingData.getString(AppConfig.KEY_BUILDIMAGES);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        BDUrl.setText(urlImage);
        buildingTitle.setText(bname);
        buildingHistory.setText(bhistory);

        String url = BDUrl.getText().toString();

        BuildingDetailsActivity.GetXMLTask task = new BuildingDetailsActivity.GetXMLTask();
        task.execute(new String[] { url });
    }

    private class GetXMLTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap map = null;
            for (String url : urls) {
                map = downloadImage(url);
            }
            return map;
        }

        // Sets the Bitmap returned by doInBackground
        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }

        // Creates Bitmap from InputStream and returns it
        private Bitmap downloadImage(String url) {
            Bitmap bitmap = null;
            InputStream stream = null;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;

            try {
                stream = getHttpConnection(url);
                bitmap = BitmapFactory.
                        decodeStream(stream, null, bmOptions);
                stream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return bitmap;
        }

        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }
    }
}

