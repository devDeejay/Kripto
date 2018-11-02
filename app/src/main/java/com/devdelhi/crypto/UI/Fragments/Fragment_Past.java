package com.devdelhi.kripto.UI.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.devdelhi.crypto.Adapter.Adapter_History_Price;
import com.devdelhi.crypto.Data.CurrencyHistory;
import com.devdelhi.crypto.Data.CurrencyHistory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import de.mateware.snacky.Snacky;

import com.devdelhi.crypto.R;

public class Fragment_Past extends Fragment {

    private Context context;
    private CurrencyHistory[] mCurrencyHistoryArray;
    private String TAG = "Past";
    private String currentCoin = "BTC";
    private String currentCurrency = "INR";
    private String toSymbol;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RelativeLayout errorLayout;
    private int numberOfDays = 30;
    private TextView pastErrorTV;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_past, container, false);

        context = view.getContext();

        recyclerView = view.findViewById(R.id.past_data_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        errorLayout = view.findViewById(R.id.pasterrorLayout);
        pastErrorTV = view.findViewById(R.id.pasterrorMessageTV);

        Typeface errorTextFont = Typeface.createFromAsset(context.getAssets(), "fonts/news_heading_font.ttf");
        pastErrorTV.setTypeface(errorTextFont);

        errorLayout.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);

        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout_past);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });

        ReadDataFromSharedPreference();

        if (isNetworkAvailable())
            refreshItems();
        else {
            errorLayout.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private void ReadDataFromSharedPreference() {
        sharedPreferences = getActivity().getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        currentCurrency = sharedPreferences.getString("CurrencyName", "USD");
        currentCoin = sharedPreferences.getString("CoinName", "BTC");
        toSymbol = sharedPreferences.getString("CurrencySymbol", "$");
        String temp = sharedPreferences.getString("Days", "30");
        numberOfDays = Integer.parseInt(temp);

        Log.d(TAG, "Read Data : " + currentCurrency + " : " + currentCoin + " : " + numberOfDays);
    }

    private void writeToSharedPreference(int num) {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("Size", num);
        editor.apply();
        Toast.makeText(getActivity(), "Showing You Data For Past " + num + " number Of Days.", Toast.LENGTH_SHORT).show();
    }

    void refreshItems() {
        mSwipeRefreshLayout.setRefreshing(true);
        recyclerView.setVisibility(View.INVISIBLE);
        getCurrencyHistory(numberOfDays);
    }

    void onItemsLoadComplete() {
        mCurrencyHistoryArray = null;
        errorLayout.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    void onItemsLoadInComplete() {
        errorLayout.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
    }


    private void getCurrencyHistory(int size) {
        final String PAST_DATA_URL = "https://min-api.cryptocompare.com/data/histoday?fsym=" + currentCoin + "&tsym=" + currentCurrency + "&limit=" + size;

        StringRequest request = new StringRequest(PAST_DATA_URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, PAST_DATA_URL + " Response Received From CCN.");
                try {
                    mCurrencyHistoryArray = parseDataForHistory(response);
                    updateDisplayForGetCurrencyHistory();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Response Failed From CCN.");

                onItemsLoadInComplete();

                Snacky.builder()
                        .setView(getActivity().findViewById(android.R.id.content))
                        .setText("Failed To Receive Data, Please Check Your Internet Connectivity")
                        .setDuration(Snacky.LENGTH_INDEFINITE)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();

            }
        });

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(request);
    }

    private CurrencyHistory[] parseDataForHistory(String response) throws JSONException {

        JSONObject parentJSONObject = new JSONObject(response);
        String message = parentJSONObject.getString("Response");
        int type = parentJSONObject.getInt("Type");

        JSONArray daysArray = parentJSONObject.getJSONArray("Data");

        String time, volumeFrom, volumeTo, close, open, high, low;

        CurrencyHistory[] dataArray = new CurrencyHistory[daysArray.length()];
        CurrencyHistory currencyHistoryObject = new CurrencyHistory();
        for (int i = 0; i < daysArray.length(); i++) {

            JSONObject tempObject = daysArray.getJSONObject(i);

             time = tempObject.getString("time");
             volumeFrom = tempObject.getString("volumefrom");
             volumeTo = tempObject.getString("volumeto");
             close = tempObject.getString("close");
             open = tempObject.getString("open");
             high = tempObject.getString("high");
             low = tempObject.getString("low");

            currencyHistoryObject.setHigh(high);
            currencyHistoryObject.setClose(close);
            currencyHistoryObject.setLow(low);
            currencyHistoryObject.setOpen(open);
            currencyHistoryObject.setVolumeTo(volumeTo);
            currencyHistoryObject.setVolumeFrom(volumeFrom);
            currencyHistoryObject.setTime(time);

            dataArray[i] = currencyHistoryObject;
        }

        return dataArray;
    }

    private void updateDisplayForGetCurrencyHistory() {

        adapter = new Adapter_History_Price(mCurrencyHistoryArray, context, toSymbol);
        recyclerView.setAdapter(adapter);

        onItemsLoadComplete();

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if ((networkInfo != null) && (networkInfo.isConnected()))
            isAvailable = true;
        return isAvailable;
    }
}