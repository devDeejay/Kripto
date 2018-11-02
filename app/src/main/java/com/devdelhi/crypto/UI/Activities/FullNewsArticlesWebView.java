package com.devdelhi.crypto.UI.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import com.devdelhi.crypto.R;

import de.mateware.snacky.Snacky;

public class FullNewsArticlesWebView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_news_articles_web_view);

        Toolbar toolbar = findViewById(R.id.news_toolbar);
        setSupportActionBar(toolbar);

        WebView webView = findViewById(R.id.webView);

        Intent intent = getIntent();
        String url = intent.getStringExtra("URL");
        String title = intent.getStringExtra("TITLE");
        String source = intent.getStringExtra("SOURCE");

        String sourceCaps = source.toUpperCase().charAt(0)+source.substring(1,source.length());

        getSupportActionBar().setTitle("News From " + sourceCaps);

        if (isNetworkAvailable()){
            webView.loadUrl(url);

            Snacky.builder()
                    .setView(findViewById(android.R.id.content))
                    .setText("Loading")
                    .setDuration(Snacky.LENGTH_SHORT)
                    .info()
                    .show();
        }

        else {
            Snacky.builder()
                    .setView(findViewById(android.R.id.content))
                    .setText("Lost Network Connection, Please Try Again Later.")
                    .setDuration(Snacky.LENGTH_INDEFINITE)
                    .setActionText(android.R.string.ok)
                    .error()
                    .show();
        }
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
