package com.example.country.country;

import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CountryActivity extends AppCompatActivity {

    TextView country_name, country_capital;
    ImageView country_flag;
    RequestBuilder<PictureDrawable> requestBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);

        country_name = findViewById(R.id.country_name);
        country_capital = findViewById(R.id.country_capital);
        country_flag = findViewById(R.id.country_flag);

        Bundle b = getIntent().getExtras();

        String apiURL = "https://restcountries.eu/rest/v2/name/" + b.getString("country-name") + "?fullText=true";
        JSONArray apiResult;

        CountryDetailsTask countryDetailsTask = new CountryDetailsTask();
        try {
            apiResult = countryDetailsTask.execute(apiURL).get();
            JSONObject country = apiResult.getJSONObject(0);

            country_name.setText(country.getString("name"));
            country_capital.setText(country.getString("capital"));

            requestBuilder = Glide.with(this)
                    .as(PictureDrawable.class)
                    .listener(new SvgSoftwareLayerSetter());

            Uri uri = Uri.parse(country.getString("flag"));
            requestBuilder.load(uri).into(country_flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class CountryDetailsTask extends AsyncTask<String, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... urls) {
            String result, inputLine;
            JSONArray jsonArray;

            try {

                URL url = new URL(urls[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
                BufferedReader reader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();

                while((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }

                result = stringBuilder.toString();
                jsonArray = new JSONArray(result);

            } catch (Exception e) {
                e.printStackTrace();
                jsonArray = null;
            }

            return jsonArray;
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            super.onPostExecute(result);
        }
    }
}
