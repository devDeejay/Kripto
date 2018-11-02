package com.devdelhi.kripto.UI.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.devdelhi.crypto.Data.Coins;
import com.devdelhi.crypto.Data.Currency;
import com.devdelhi.crypto.UI.Activities.SelectCurrency;
import com.devdelhi.crypto.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.mateware.snacky.Snacky;

public class Fragment_Present extends Fragment {

    @BindView(R.id.riseTodayTV)
    TextView riseTodayTV;
    @BindView(R.id.changeCurrencyButton)
    Button changeCurrencyButton;
    @BindView(R.id.priceTV)
    TextView priceTV;
    @BindView(R.id.fromSymbol)
    TextView fromSymbolTV;
    @BindView(R.id.marketName)
    TextView marketNameTV;
    @BindView(R.id.lastUpdateTV)
    TextView lastUpdateTV;
    @BindView(R.id.lastVolumeTV)
    TextView lastVolumeTV;
    @BindView(R.id.lastVolumeToTV)
    TextView lastVolumeToTV;
    @BindView(R.id.volumeDayTV)
    TextView volumeDayTV;
    @BindView(R.id.volumeDayToTV)
    TextView volumeDayToTV;
    @BindView(R.id.volume24HourTV)
    TextView volume24HourTV;
    @BindView(R.id.volume24HourTo)
    TextView volume24HourTo;
    @BindView(R.id.openDay)
    TextView openDayTV;
    @BindView(R.id.highDay)
    TextView highDayTV;
    @BindView(R.id.lowDay)
    TextView lowDayTV;
    @BindView(R.id.open24Hour)
    TextView open24HourTV;
    @BindView(R.id.high24Hour)
    TextView high24HourTV;
    @BindView(R.id.low24Hour)
    TextView low24HourTV;
    @BindView(R.id.lastMarket)
    TextView lastMarketTV;
    @BindView(R.id.change24Hour)
    TextView change24HourTV;
    @BindView(R.id.supply)
    TextView supplyTV;
    @BindView(R.id.marketCap)
    TextView marketCapTV;
    @BindView(R.id.totalVolume24H)
    TextView totalVolume24HTV;
    @BindView(R.id.totalVolume24HoursTo)
    TextView totalVolume24HoursToTV;
    @BindView(R.id.changePctDate)
    TextView changePctDateTV;
    @BindView(R.id.changePCT24Hour)
    TextView changePCT24HourTV;
    @BindView(R.id.changeDay)
    TextView changeDayTV;
    @BindView(R.id.errorTextView)
    TextView errorText;
    @BindView(R.id.progressLoaderForPresent)
    LinearLayout loader;

    private String TAG = "DEEJAY";
    private String[] currencyArray = {"INR"};
    private String[] coinsArray = {"BTC"};
    private int currencyArraySize = 0;
    private int coinsArraySize = 0;
    private Coins[] finalReceivedCoinDataArray;
    private Unbinder unbinder;
    private SharedPreferences sharedPreferences;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RelativeLayout presentDataRelativeLayout;
    private RelativeLayout errorLayout;
    private ScrollView scrollview;
    private String apiURL;
    private String toCurrencySymbol;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_present, container, false);
        Context context = view.getContext();
        ButterKnife.bind(this, view);
        unbinder = ButterKnife.bind(this, view);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayoutPresent);
        presentDataRelativeLayout = view.findViewById(R.id.presentDataFullLayout);
        scrollview = view.findViewById(R.id.scrollView2);
        errorLayout = view.findViewById(R.id.errorLayout);

        Typeface mediumBoldFont = Typeface.createFromAsset(context.getAssets(), "fonts/mediumbold.ttf");
        Typeface errorTextFont = Typeface.createFromAsset(context.getAssets(), "fonts/news_heading_font.ttf");
        priceTV.setTypeface(mediumBoldFont);
        errorText.setTypeface(errorTextFont);

        ReadDataFromSharedPreference();

        currencyArraySize = currencyArray.length;
        coinsArraySize = coinsArray.length;

        String currencies = "";
        String cryptocoins = "";

        for (String currency : currencyArray) {
            currencies += currency;
            currencies += ",";
        }

        for (String coins : coinsArray) {
            cryptocoins += coins;
            cryptocoins += ",";
        }

        apiURL = "https://min-api.cryptocompare.com/data/pricemultifull?fsyms="
                + cryptocoins + "&tsyms=" + currencies;

        Log.d(TAG, "Final URL Formed Is " + apiURL);

        loadDataInFragment();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDataInFragment();
            }
        });

        changeCurrencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity().getApplicationContext(), SelectCurrency.class);
                startActivity(i);
            }
        });

        return view;
    }

    private void loadDataInFragment() {
        errorLayout.setVisibility(View.INVISIBLE);
        scrollview.setVisibility(View.INVISIBLE);
        presentDataRelativeLayout.setVisibility(View.INVISIBLE);
        loader.setVisibility(View.INVISIBLE);

        if (isNetworkAvailable())
            refreshItems();
        else {
            errorLayout.setVisibility(View.VISIBLE);
            onItemsLoadInComplete();
        }
    }


    void refreshItems() {
        presentDataRelativeLayout.setVisibility(View.INVISIBLE);
        mSwipeRefreshLayout.setRefreshing(true);
        getDataFromServer();
    }

    void onItemsLoadComplete() {
        errorLayout.setVisibility(View.INVISIBLE);
        scrollview.setVisibility(View.VISIBLE);
        presentDataRelativeLayout.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    void onItemsLoadInComplete() {
        errorLayout.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void ReadDataFromSharedPreference() {
        sharedPreferences = getActivity().getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        currencyArray[0] = sharedPreferences.getString("CurrencyName", "USD");
        coinsArray[0] = sharedPreferences.getString("CoinName", "BTC");
        toCurrencySymbol = sharedPreferences.getString("CurrencySymbol", "$");

        Log.d(TAG, "Getting Data For : " + currencyArray[0] + " : " + coinsArray[0]);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getDataFromServer() {
        StringRequest request = new StringRequest(apiURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                try {
                    Log.d(TAG, "Response Received From Server.");
                    finalReceivedCoinDataArray = parseData(response);
                    if (finalReceivedCoinDataArray == null) {
                        Snacky.builder()
                                .setView(getActivity().findViewById(android.R.id.content))
                                .setText("Malformed Request, Please Check Your Inputs")
                                .setDuration(Snacky.LENGTH_INDEFINITE)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();

                        Log.d(TAG, "No Data Received");
                    } else {
                        Log.d(TAG, "Data Received, Trying To Display");

                        Log.d(TAG, "Must Work Now.");
                        updateDisplay();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Catching Exception " + e.getMessage());
                    onItemsLoadInComplete();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Failed To Get Data");
                Snacky.builder()
                        .setView(getActivity().findViewById(android.R.id.content))
                        .setText("Failed To Get Data")
                        .setDuration(Snacky.LENGTH_INDEFINITE)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(request);
    }

    private void updateDisplay() {
        Log.d(TAG, "Update Display");
        for (int i = 0; i < finalReceivedCoinDataArray.length; i++) {
            Coins cryptoCoin = finalReceivedCoinDataArray[i];
            Currency[] userCurrencyArray = cryptoCoin.getDataArray();

            for (int j = 0; j < userCurrencyArray.length; j++) {
                Currency currencyDetails = userCurrencyArray[j];

                // String tosymbol = currencyDetails.getTOSYMBOL();

                lastUpdateTV.setText(currencyDetails.getLASTUPDATE() + "");
                priceTV.setText(currencyDetails.getPRICE() + "");
                riseTodayTV.setText(currencyDetails.getCHANGEDAY() + "");
                changeDayTV.setText(currencyDetails.getCHANGEDAY() + "");
                open24HourTV.setText(currencyDetails.getOPEN24HOUR() + "");
                openDayTV.setText(currencyDetails.getOPENDAY() + "");
                lowDayTV.setText(currencyDetails.getLOWDAY() + "");
                highDayTV.setText(currencyDetails.getHIGHDAY() + "");
                high24HourTV.setText(currencyDetails.getHIGH24HOUR() + "");
                low24HourTV.setText(currencyDetails.getLOW24HOUR() + "");
                change24HourTV.setText(currencyDetails.getCHANGE24HOUR() + "");
                lastMarketTV.setText(currencyDetails.getLASTMARKET() + "");
                marketNameTV.setText(currencyDetails.getMARKET() + "");
                fromSymbolTV.setText(currencyDetails.getFROMSYMBOL() + "");
                lastVolumeTV.setText(currencyDetails.getLASTVOLUME() + "");
                lastVolumeToTV.setText(currencyDetails.getLASTVOLUMETO() + "");
                volumeDayTV.setText(currencyDetails.getVOLUMEDAY() + "");
                volumeDayToTV.setText(currencyDetails.getVOLUMEDAYTO() + "");
                volume24HourTV.setText(currencyDetails.getVOLUME24HOUR() + "");
                volume24HourTo.setText(currencyDetails.getVOLUME24HOURTO() + "");
                totalVolume24HTV.setText(currencyDetails.getTOTALVOLUME24H() + "");
                totalVolume24HoursToTV.setText(currencyDetails.getTOTALVOLUME24HTO() + "");
                marketCapTV.setText(currencyDetails.getMKTCAP() + "");
                supplyTV.setText(currencyDetails.getSUPPLY() + "");
                changePCT24HourTV.setText(currencyDetails.getCHANGEPCT24HOUR() + "");
                changePctDateTV.setText(currencyDetails.getCHANGEPCTDAY() + "");

                onItemsLoadComplete();
            }
        }
    }

    private Coins[] parseData(String jsonData) throws JSONException {

        Log.d(TAG, "Parsing Data");

        JSONObject parentJSONObject = new JSONObject(jsonData);

        JSONObject displayObject = parentJSONObject.getJSONObject("RAW");
        Coins[] coinsDataArray = new Coins[coinsArraySize];

        Coins coins = new Coins();
        Currency currentCurrency = new Currency();

        long PRICE, LASTUPDATE, LASTVOLUME, LASTVOLUMETO, VOLUMEDAY, VOLUMEDAYTO,
                VOLUME24HOUR, VOLUME24HOURTO, OPENDAY, OPEN24HOUR, HIGHDAY, LOWDAY,
                HIGH24HOUR, LOW24HOUR, CHANGE24HOUR, CHANGEPCT24HOUR, CHANGEDAY,
                CHANGEPCTDAY, SUPPLY, MKTCAP, TOTALVOLUME24H, TOTALVOLUME24HTO;

        String MARKET, FROMSYMBOL, TOSYMBOL, LASTMARKET;

        for (int i = 0; i < coinsArraySize; i++) {
            JSONObject coinObject = displayObject.getJSONObject(coinsArray[i]);
            Currency[] currencyDataArrayForEachCoin = new Currency[currencyArraySize];
            for (int j = 0; j < currencyArraySize; j++) {

                JSONObject currencyObject = coinObject.getJSONObject(currencyArray[j]);

                MARKET = currencyObject.getString("MARKET");
                FROMSYMBOL = currencyObject.getString("FROMSYMBOL");
                TOSYMBOL = currencyObject.getString("TOSYMBOL");
                LASTMARKET = currencyObject.getString("LASTMARKET");

                PRICE = currencyObject.getLong("PRICE");
                LASTUPDATE = currencyObject.getLong("LASTUPDATE");
                LASTVOLUME = currencyObject.getLong("LASTVOLUME");
                LASTVOLUMETO = currencyObject.getLong("LASTVOLUMETO");
                VOLUMEDAY = currencyObject.getLong("VOLUMEDAY");
                VOLUMEDAYTO = currencyObject.getLong("VOLUMEDAYTO");
                VOLUME24HOUR = currencyObject.getLong("VOLUME24HOUR");
                VOLUME24HOURTO = currencyObject.getLong("VOLUME24HOURTO");
                OPENDAY = currencyObject.getLong("OPENDAY");
                HIGHDAY = currencyObject.getLong("HIGHDAY");
                LOWDAY = currencyObject.getLong("LOWDAY");
                OPEN24HOUR = currencyObject.getLong("OPEN24HOUR");
                HIGH24HOUR = currencyObject.getLong("HIGH24HOUR");
                LOW24HOUR = currencyObject.getLong("LOW24HOUR");
                CHANGE24HOUR = currencyObject.getLong("CHANGE24HOUR");
                CHANGEPCT24HOUR = currencyObject.getLong("CHANGEPCT24HOUR");
                CHANGEDAY = currencyObject.getLong("CHANGEDAY");
                CHANGEPCTDAY = currencyObject.getLong("CHANGEPCTDAY");
                SUPPLY = currencyObject.getLong("SUPPLY");
                MKTCAP = currencyObject.getLong("MKTCAP");
                TOTALVOLUME24H = currencyObject.getLong("TOTALVOLUME24H");
                TOTALVOLUME24HTO = currencyObject.getLong("TOTALVOLUME24HTO");

                currentCurrency.setMARKET(MARKET);
                currentCurrency.setFROMSYMBOL(FROMSYMBOL);
                currentCurrency.setTOSYMBOL(TOSYMBOL);
                currentCurrency.setLASTMARKET(LASTMARKET);

                currentCurrency.setLASTUPDATE(LASTUPDATE);
                currentCurrency.setPRICE(PRICE);
                currentCurrency.setLASTVOLUMETO(LASTVOLUMETO);
                currentCurrency.setLASTVOLUME(LASTVOLUME);
                currentCurrency.setVOLUMEDAY(VOLUMEDAY);
                currentCurrency.setVOLUMEDAYTO(VOLUMEDAYTO);
                currentCurrency.setVOLUME24HOUR(VOLUME24HOUR);
                currentCurrency.setVOLUME24HOURTO(VOLUME24HOURTO);
                currentCurrency.setOPENDAY(OPENDAY);
                currentCurrency.setOPEN24HOUR(OPEN24HOUR);
                currentCurrency.setLOWDAY(LOWDAY);
                currentCurrency.setCHANGE24HOUR(CHANGE24HOUR);
                currentCurrency.setCHANGEDAY(CHANGEDAY);
                currentCurrency.setCHANGEPCT24HOUR(CHANGEPCT24HOUR);
                currentCurrency.setCHANGEPCTDAY(CHANGEPCTDAY);
                currentCurrency.setHIGH24HOUR(HIGH24HOUR);
                currentCurrency.setHIGHDAY(HIGHDAY);
                currentCurrency.setTOTALVOLUME24H(TOTALVOLUME24H);
                currentCurrency.setTOTALVOLUME24HTO(TOTALVOLUME24HTO);
                currentCurrency.setMKTCAP(MKTCAP);
                currentCurrency.setSUPPLY(SUPPLY);
                currentCurrency.setLOW24HOUR(LOW24HOUR);

                currencyDataArrayForEachCoin[j] = currentCurrency;
            }

            coins.setDataArray(currencyDataArrayForEachCoin);
            coinsDataArray[i] = coins;
        }

        return coinsDataArray;
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
