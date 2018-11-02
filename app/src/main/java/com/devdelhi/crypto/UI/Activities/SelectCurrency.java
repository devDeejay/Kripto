package com.devdelhi.crypto.UI.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.devdelhi.crypto.R;

import org.json.JSONException;
import org.json.JSONObject;

import de.mateware.snacky.Snacky;

public class SelectCurrency extends AppCompatActivity {

    private String currencyName;
    private String cryptocurrencyName;
    private String currencySymbol;
    private String pastDataLength;
    private EditText currencyNameET, cryptocurrencyNameET, pastDataLengthET;
    private TextView headingTV, fromTV, toTV, daysTV;
    private Button changeCurrencyButton;
    private SharedPreferences sharedPreferences;
    private String TAG = "SELECT_CURRENCY";
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_currency);

        ReadDataFromSharedPreference();

        changeCurrencyButton = findViewById(R.id.changeCurrencyButton);
        currencyNameET = findViewById(R.id.selectC);
        cryptocurrencyNameET = findViewById(R.id.selectCC);
        pastDataLengthET = findViewById(R.id.selectDays);
        headingTV = findViewById(R.id.headingTV);
        fromTV = findViewById(R.id.from);
        toTV = findViewById(R.id.to);
        daysTV = findViewById(R.id.days);

        Typeface news_heading_font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/news_heading_font.ttf");
        Typeface semi_bold_font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/semibold.ttf");
        headingTV.setTypeface(news_heading_font);
        fromTV.setTypeface(semi_bold_font);
        toTV.setTypeface(semi_bold_font);
        daysTV.setTypeface(semi_bold_font);

        currencyNameET.setText(currencyName);
        cryptocurrencyNameET.setText(cryptocurrencyName);
        pastDataLengthET.setText(pastDataLength);

        changeCurrencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cryptocurrencyName = cryptocurrencyNameET.getText().toString().toUpperCase().trim();
                currencyName = currencyNameET.getText().toString().toUpperCase().trim();
                pastDataLength = pastDataLengthET.getText().toString().trim();

                if (isNetworkAvailable()) {

                    if (!(TextUtils.isEmpty(cryptocurrencyName) || TextUtils.isEmpty(currencyName) || TextUtils.isEmpty(pastDataLength) )){
                        try {
                            int temp = Integer.parseInt(pastDataLength); //Checking Validity Of Number Input
                            writeToSharedPreference("Days", pastDataLength + "");
                            checkDataFromServer(cryptocurrencyName, currencyName);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            Snacky.builder()
                                    .setView(findViewById(android.R.id.content))
                                    .setText("Invalid Inputs")
                                    .setDuration(Snacky.LENGTH_INDEFINITE)
                                    .setActionText(android.R.string.ok)
                                    .error()
                                    .show();
                        }
                    }
                    else {
                        Snacky.builder()
                                .setActivity(SelectCurrency.this)
                                .setText("Please, Leave No Fields Empty")
                                .setDuration(Snacky.LENGTH_LONG)
                                .warning()
                                .show();
                    }
                }else{
                    Snacky.builder()
                            .setActivity(SelectCurrency.this)
                            .setText("Internet Connection Is Required To Change Your Currency")
                            .setDuration(Snacky.LENGTH_LONG)
                            .warning()
                            .show();
                }
            }
        });
    }

    private void ReadDataFromSharedPreference() {
        sharedPreferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        currencyName = sharedPreferences.getString("CurrencyName", "USD");
        currencySymbol = sharedPreferences.getString("CurrencySymbol", "");
        cryptocurrencyName = sharedPreferences.getString("CoinName", "BTC");
        pastDataLength = sharedPreferences.getString("Days", "30");

        Log.d(TAG, "Read Data : " + currencyName + " : " + cryptocurrencyName);
    }

    private void checkDataFromServer(final String cryptocurrencyName, final String currencyName) {
        String apiURL = "https://min-api.cryptocompare.com/data/pricemultifull?fsyms="
                + cryptocurrencyName + "&tsyms=" + currencyName;
        StringRequest request = new StringRequest(apiURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                try {
                    Log.d(TAG, "Response Received");
                    if (checkReturnedData(response)) {
                        Log.d(TAG, "Data Received, Changing Settings");

                        Log.d(TAG, "This is true");
                        writeToSharedPreference("CoinName", cryptocurrencyName);
                        writeToSharedPreference("CurrencyName", currencyName);
                        writeToSharedPreference("CurrencySymbol", currencySymbol);

                        Intent i = new Intent(SelectCurrency.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);

                    } else {
                        Log.d(TAG, "Data Not Received");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    Snacky.builder()
                            .setView(findViewById(android.R.id.content))
                            .setText("Sorry, There is something wrong with your Inputs, Or We Just Don't Support The Entered Currencies Yet!")
                            .setDuration(Snacky.LENGTH_INDEFINITE)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();

                    Log.d(TAG, "Catching Exception " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Failed To Get Data");

                Snacky.builder()
                        .setView(findViewById(android.R.id.content))
                        .setText("Something Went Wrong!")
                        .setDuration(Snacky.LENGTH_INDEFINITE)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }


    private boolean checkReturnedData(String jsonData) throws JSONException {
        Log.d(TAG, "Parsing Data");
        JSONObject parentJSONObject = new JSONObject(jsonData);
        JSONObject displayObject = parentJSONObject.getJSONObject("DISPLAY");
        if (displayObject != null) {
            String tempSymbol = displayObject.getJSONObject(cryptocurrencyName).getJSONObject(currencyName).getString("TOSYMBOL");
            Log.d(TAG, "Temp Symbol is" + tempSymbol);
            if (tempSymbol != null){
                currencySymbol = tempSymbol;
                Log.d(TAG, "Returning True From CheckReturnedData" + tempSymbol);
                return true;
            }
        }
        return false;
    }

    private void writeToSharedPreference(String keyString, String keyValue) {
        SharedPreferences sharedPref = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(keyString, keyValue);
        editor.apply();

        Log.d(TAG, "Added Data : " + keyString + " : " + keyValue + " to Shared Preferences");
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if ((networkInfo != null) && (networkInfo.isConnected()))
            isAvailable = true;
        return isAvailable;
    }
}
