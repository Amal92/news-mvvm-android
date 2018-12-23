package com.amp.news.UI.Activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.amp.news.BuildConfig;
import com.amp.news.R;
import com.amp.news.UI.Fragments.NewsFragment;
import com.amp.news.UI.Fragments.WeatherFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    int selectedMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_news_latest);
        selectedMenuItem = R.id.nav_news_latest;
        gotoFragment(NewsFragment.newInstance("latest"));
        setTitle(getString(R.string.latest_news));
        View headerView = navigationView.getHeaderView(0);
        TextView version_tv = headerView.findViewById(R.id.version_tv);
        String version = "Version " + BuildConfig.VERSION_NAME;
        version_tv.setText(version);
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.


        drawer.closeDrawer(GravityCompat.START);
        selectFragmentForDisplay(item);
        return true;
    }

    private void selectFragmentForDisplay(MenuItem menuItem) {
        Fragment fragment = null;
        if (menuItem.getItemId() == selectedMenuItem)
            return;
        else selectedMenuItem = menuItem.getItemId();

        switch (menuItem.getItemId()) {
            case R.id.nav_news_latest:
                setTitle(getString(R.string.latest_news));
                fragment = NewsFragment.newInstance("latest");
                break;
            case R.id.nav_news_business:
                setTitle(getString(R.string.business_news));
                fragment = NewsFragment.newInstance("business");
                break;
            case R.id.nav_news_science:
                setTitle(getString(R.string.science_news));
                fragment = NewsFragment.newInstance("science");
                break;
            case R.id.nav_news_sports:
                setTitle(getString(R.string.sports_news));
                fragment = NewsFragment.newInstance("sports");
                break;
            case R.id.nav_news_tech:
                setTitle(getString(R.string.tech_news));
                fragment = NewsFragment.newInstance("technology");
                break;
            case R.id.nav_weather:
                setTitle(getString(R.string.weather));
                fragment = WeatherFragment.newInstance();
                break;

        }

        gotoFragment(fragment);

    }

    private void gotoFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

}
