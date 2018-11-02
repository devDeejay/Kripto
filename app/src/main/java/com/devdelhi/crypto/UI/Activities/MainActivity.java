package com.devdelhi.crypto.UI.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.devdelhi.crypto.Adapter.SectionsPagerAdapter;
import com.devdelhi.crypto.R;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "real";
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionPageAdapter;
    private String username;
    private String currencySymbol;
    private TextView usernameTV;
    private SharedPreferences sharedPreferences;
    private String cryptocurrencyName;
    private String currencyName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ReadDataFromSharedPreference();


        if (false){ //firebaseUser != null
            Log.d(TAG, "Is null");
            finish();
        }
        else {
            Log.d(TAG, "Not null");

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Kripto   ( " + cryptocurrencyName + " / " + currencyName+" )");

            mSectionPageAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

            mViewPager = findViewById(R.id.container);
            mViewPager.setOffscreenPageLimit(3);
            setupViewPager(mViewPager);

            TabLayout tabLayout = findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            View headerView = navigationView.getHeaderView(0);

            TextView appNameTV = headerView.findViewById(R.id.app_name_tv);
            TextView devdelhiTV = headerView.findViewById(R.id.devdelhi_name_tv);

            Typeface news_heading_font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/news_heading_font.ttf");
            Typeface semibold = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/semibold.ttf");
            appNameTV.setTypeface(news_heading_font);
            devdelhiTV.setTypeface(semibold);

        }

    }

    private void ReadDataFromSharedPreference() {
        sharedPreferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        //username = sharedPreferences.getString("UserName", "User");
        currencyName = sharedPreferences.getString("CurrencyName", "USD");
        cryptocurrencyName = sharedPreferences.getString("CoinName", "BTC");
        currencySymbol = sharedPreferences.getString("CurrencySymbol", "BTC");
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new com.devdelhi.kripto.UI.Fragments.Fragment_Present(), "Present");
        adapter.addFragment(new com.devdelhi.kripto.UI.Fragments.Fragment_Past(), "Past");
        adapter.addFragment(new com.devdelhi.kripto.UI.Fragments.Fragment_News(), "News");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.change_currency) {
            startActivity(new Intent(MainActivity.this, SelectCurrency.class));
            // Handle the camera action
        }

        else if (id == R.id.cryptocompare) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.cryptocompare.com/"));
            startActivity(i);
        }

        else if (id == R.id.more_apps) {
            Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Dev+Delhi"));
            startActivity(rateIntent);
        }
        else if (id == R.id.nav_share) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Choose Your Preference For Sharing", Snackbar.LENGTH_LONG);
            snackbar.show();

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey! Check Out This Amazing App 'Kripto' If You Are Into CryptoCurrency : https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);


        } else if (id == R.id.nav_send) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Opening Your Mail Service", Snackbar.LENGTH_LONG);
            snackbar.show();
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback For Kripto");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Hey DevDelhi!");
            emailIntent.setData(Uri.parse("mailto:delhidev10@gmail.com"));
            startActivity(emailIntent);

        } else if (id == R.id.nav_rate) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Opening Playstore", Snackbar.LENGTH_LONG);
            snackbar.show();
            Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getApplicationContext().getPackageName()));
            startActivity(rateIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
