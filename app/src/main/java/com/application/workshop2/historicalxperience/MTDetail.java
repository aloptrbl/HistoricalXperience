package com.application.workshop2.historicalxperience;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
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
import java.util.ArrayList;

public class MTDetail extends AppCompatActivity {

    TextView idR;
    TextView mtName,mtDesc,mtURL, mtAddress;
    ProgressDialog loading;
    String id;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mtdetail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        Intent in = this.getIntent();
        String id = in.getStringExtra("ItemPosition");
        idR = (TextView) findViewById(R.id.mt_id);
        idR.setText(id);
        mtName = (TextView) findViewById(R.id.mt_name);
        mtDesc = (TextView) findViewById(R.id.mt_summary);
        imageView = (ImageView) findViewById(R.id.mt_image);
        mtURL = (TextView) findViewById(R.id.mt_url);
        mtAddress = (TextView) findViewById(R.id.mt_address);
        getData();

        idR.setVisibility(View.GONE);
        mtURL.setVisibility(View.GONE);
    }


    public void getData(){
        String idRecipe = idR.getText().toString().trim();

        if (mtName.equals("")) {
            Toast.makeText(this, "Please enter an id", Toast.LENGTH_LONG).show();
            return;
        }
        loading = ProgressDialog.show(this,"Please wait...","Fetching...",false,false);

        String url = AppConfig.URL_buildingDetail+idRecipe;

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
                        Toast.makeText(MTDetail.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response){
        String nameMT="";
        String historyMT="";
        String urlImage = "";
        String addressMT = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(AppConfig.JSON_ARRAY);
            JSONObject data = result.getJSONObject(0);
            nameMT = data.getString(AppConfig.KEY_BUILDNAMEDETAIL);
            historyMT = data.getString(AppConfig.KEY_BUILDHISTORYDETAIL);
            urlImage = data.getString(AppConfig.KEY_URLDETAIL);
            addressMT = data.getString(AppConfig.KEY_BUILDADD);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        mtName.setText(nameMT);
        mtDesc.setText(historyMT);
        mtURL.setText(urlImage);
        mtAddress.setText(addressMT);

        String url = mtURL.getText().toString();

        GetXMLTask task = new GetXMLTask();
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
