package com.devdelhi.kripto.UI.Fragments;

import android.content.Context;
import android.content.Intent;
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
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.devdelhi.crypto.Adapter.NewsAdapter;
import com.devdelhi.crypto.Data.NewsData;
import com.devdelhi.crypto.UI.Activities.FullNewsArticlesWebView;
import com.devdelhi.crypto.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import de.mateware.snacky.Snacky;

public class Fragment_News extends Fragment {

    private Context context;
    private String TAG = "DJ_News";
    private NewsData mNewsData = new NewsData();
    private NewsAdapter.OnItemClickListener listener;
    private ArrayList<NewsData> allNewsArray;
    private NewsData[] CCN_NEWS_DATA_ARRAY;
    private NewsData[] CC_NEWS_DATA_ARRAY;
    private NewsData[] FINAL_DATA_ARRAY;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RelativeLayout errorLayout;
    private TextView errorMessageTV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        context = view.getContext();
        errorLayout = view.findViewById(R.id.newsErrorLayout);
        mSwipeRefreshLayout = view.findViewById(R.id.newsSwipeRefreshLayoutNews);
        recyclerView = view.findViewById(R.id.news_data_recycler_view);
        errorMessageTV = view.findViewById(R.id.newsErrorMessageTV);

        Typeface errorTextFont = Typeface.createFromAsset(context.getAssets(), "fonts/news_heading_font.ttf");
        errorMessageTV.setTypeface(errorTextFont);

        errorLayout.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        allNewsArray = new ArrayList<>();

        if (isNetworkAvailable())
            refreshItems();
        else {
            errorLayout.setVisibility(View.VISIBLE);
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });

        return view;
    }


    private void getNewsFromCCN() {
        String CCN_URL = "https://newsapi.org/v2/top-headlines?sources=crypto-coins-news&apiKey=7dbc2f072c834292802b3f5f9ebbd9ad";
        String CC_API_URL = "https://min-api.cryptocompare.com/data/v2/news/?lang=EN";

        Log.d(TAG, CCN_URL);
        Log.d(TAG, CC_API_URL);

        StringRequest request = new StringRequest(CCN_URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Response Received From CCN.");
                try {
                    CCN_NEWS_DATA_ARRAY = parseDataForCCNNews(response);
                    if (CCN_NEWS_DATA_ARRAY == null) {
                        Snacky.builder()
                                .setView(getActivity().findViewById(android.R.id.content))
                                .setText("Failed To Receive Data From One Of The News Providers")
                                .setDuration(Snacky.LENGTH_INDEFINITE)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();

                        CCN_NEWS_DATA_ARRAY = new NewsData[0];

                    }
                } catch (JSONException e) {
                    Log.d(TAG, "Error Occurred While Parsing Data CC");
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Response Failed From CCN.");
                Snacky.builder()
                        .setView(getActivity().findViewById(android.R.id.content))
                        .setText("Failed To Receive Data From One Of The News Providers")
                        .setDuration(Snacky.LENGTH_INDEFINITE)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });

        StringRequest CC_API_REQUEST = new StringRequest(CC_API_URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Response Received From CC_API_URL.");
                try {
                    CC_NEWS_DATA_ARRAY = parseDataForCC_API_URL(response);
                    if (CC_NEWS_DATA_ARRAY == null) {
                        Snacky.builder()
                                .setView(getActivity().findViewById(android.R.id.content))
                                .setText("Failed To Receive Data From One Of The News Providers")
                                .setDuration(Snacky.LENGTH_INDEFINITE)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();

                        CC_NEWS_DATA_ARRAY = new NewsData[0];

                    }
                } catch (JSONException e) {

                    Log.d(TAG, "Error Occurred While Parsing Data CC");
                    e.printStackTrace();
                }
                updateDisplayForNewsData();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                onItemsLoadInComplete();

                Snacky.builder()
                        .setView(getActivity().findViewById(android.R.id.content))
                        .setText("Failed To Receive Data From One Of The News Providers")
                        .setDuration(Snacky.LENGTH_INDEFINITE)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();

                updateDisplayForNewsData();
                Log.d(TAG, "Response Failed From CC_API_URL.");
            }
        });


        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(request);
        queue.add(CC_API_REQUEST);

    }

    private NewsData[] parseDataForCC_API_URL(String response) throws JSONException {

        JSONObject parentJSONObject = new JSONObject(response);
        String message = parentJSONObject.getString("Message");
        int totalResults = parentJSONObject.getInt("Type");

        JSONArray articlesArray = parentJSONObject.getJSONArray("Data");

        NewsData[] NewsDataArray = new NewsData[articlesArray.length()];

        String title, description, url, urlToImage, date, source;

        for (int i = 0; i < articlesArray.length(); i++) {
            NewsData newsData = new NewsData();

            JSONObject tempObject = articlesArray.getJSONObject(i);

            title = tempObject.getString("title");
            description = tempObject.getString("body");
            url = tempObject.getString("url");
            urlToImage = tempObject.getString("imageurl");
            date = tempObject.getString("published_on");
            source = tempObject.getString("source");

            newsData.setTitle(title);
            newsData.setDescription(description);
            newsData.setUrl(url);
            newsData.setUrlToImage(urlToImage);
            newsData.setDate(date);
            newsData.setSource(source);

            NewsDataArray[i] = newsData;
        }

        return NewsDataArray;
    }

    private void updateDisplayForNewsData() {

        int len1, len2;

        if (CC_NEWS_DATA_ARRAY == null) {
            len1 = 0;
            CC_NEWS_DATA_ARRAY = new NewsData[len1];
        } else {
            len1 = CC_NEWS_DATA_ARRAY.length;
        }

        if (CCN_NEWS_DATA_ARRAY == null) {
            len2 = 0;
            CCN_NEWS_DATA_ARRAY = new NewsData[len2];
        } else {
            len2 = CCN_NEWS_DATA_ARRAY.length;
        }

        final NewsData[] FINAL_DATA_ARRAY = Arrays.copyOf(CC_NEWS_DATA_ARRAY, len1 + len2);
        System.arraycopy(CCN_NEWS_DATA_ARRAY, 0, FINAL_DATA_ARRAY, len1, len2);

        Log.d(TAG, "Ready To Update For News");
        adapter = new NewsAdapter(FINAL_DATA_ARRAY, context, listener);

        final GestureDetector mGestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {

                return super.onDoubleTap(e);
            }

        });

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    int position = recyclerView.getChildAdapterPosition(child);
                    String url = FINAL_DATA_ARRAY[position].getUrl();
                    String title = FINAL_DATA_ARRAY[position].getTitle();
                    String src = FINAL_DATA_ARRAY[position].getSource();
                    Intent i = new Intent(getActivity().getApplicationContext(), FullNewsArticlesWebView.class);
                    i.putExtra("URL", url);
                    i.putExtra("TITLE", title);
                    i.putExtra("SOURCE", src);
                    startActivity(i);
                    return true;
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        recyclerView.setAdapter(adapter);
        onItemsLoadComplete();
    }

    private NewsData[] parseDataForCCNNews(String jsonData) throws JSONException {

        JSONObject parentJSONObject = new JSONObject(jsonData);
        String status = parentJSONObject.getString("status");
        int totalResults = parentJSONObject.getInt("totalResults");

        String title, description, url, urlToImage, date;

        JSONArray articlesArray = parentJSONObject.getJSONArray("articles");

        NewsData[] NewsDataArray = new NewsData[articlesArray.length()];

        for (int i = 0; i < articlesArray.length(); i++) {

            NewsData newsData = new NewsData();

            JSONObject tempObject = articlesArray.getJSONObject(i);

            title = tempObject.getString("title");
            description = tempObject.getString("description");
            url = tempObject.getString("url");
            urlToImage = tempObject.getString("urlToImage");
            date = tempObject.getString("publishedAt");

            newsData.setTitle(title);
            newsData.setDescription(description);
            newsData.setUrl(url);
            newsData.setUrlToImage(urlToImage);
            newsData.setDate(date);
            newsData.setSource("Crypto Currency News");

            NewsDataArray[i] = newsData;
        }

        return NewsDataArray;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if ((networkInfo != null) && (networkInfo.isConnected()))
            isAvailable = true;
        return isAvailable;
    }

    void refreshItems() {
        mSwipeRefreshLayout.setRefreshing(true);
        getNewsFromCCN();
    }

    void onItemsLoadComplete() {
        allNewsArray = null;
        mSwipeRefreshLayout.setRefreshing(false);
        errorLayout.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    void onItemsLoadInComplete() {
        errorLayout.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
    }
}