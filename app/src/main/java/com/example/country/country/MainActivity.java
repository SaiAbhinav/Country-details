package com.example.country.country;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.BundleCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner cSpinner;
    Button cButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cSpinner = findViewById(R.id.cSpinner);
        cButton = findViewById(R.id.cButton);

        ArrayList<String> countries = new ArrayList<>();

        String apiURL = "https://restcountries.eu/rest/v2/all?fields=name;";
        JSONArray apiResult;

        cSpinner.setOnItemSelectedListener(this);

        CountryTask countryTask = new CountryTask();
        try {
            apiResult = countryTask.execute(apiURL).get();
            JSONObject country;

            for (int i=0; i<apiResult.length(); i++) {
                country = apiResult.getJSONObject(i);
                countries.add(country.getString("name"));
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countries);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cSpinner.setAdapter(arrayAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }

        cButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CountryActivity.class);
                Bundle b = new Bundle();
                b.putString("country-name", String.valueOf(cSpinner.getSelectedItem()));
                i.putExtras(b);
                startActivity(i);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class CountryTask extends AsyncTask<String, Void, JSONArray> {

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
