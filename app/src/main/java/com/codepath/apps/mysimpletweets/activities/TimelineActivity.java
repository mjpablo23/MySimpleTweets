package com.codepath.apps.mysimpletweets.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.ProfileActivity;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.MentionsTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.SmartFragmentStatePagerAdapter;
import com.codepath.apps.mysimpletweets.fragments.TweetsListFragment;

import java.io.IOException;

public class TimelineActivity extends AppCompatActivity {

    // compose activity
    private final int REQUEST_CODE_COMPOSE = 20;

    // Instance of the progress action-view
    MenuItem miActionProgressItem;

    // pager adapter
    TweetsPagerAdapter tweetsPagerAdapter;
    ViewPager vpPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // set toolbar icon
        toolbar.setTitle("Super Twitter");
        toolbar.setNavigationIcon(R.drawable.super_twitter_small);
        setSupportActionBar(toolbar);

        // get viewpager
        vpPager = (ViewPager) findViewById(R.id.viewpager);
        // set the view pager adapter for the pager
        tweetsPagerAdapter = new TweetsPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(tweetsPagerAdapter);
        // find the sliding tabstrip
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // attach the tabstrip to the view pager
        tabStrip.setViewPager(vpPager);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        // Extract the action-view from the menu item
        ProgressBar v =  (ProgressBar) MenuItemCompat.getActionView(miActionProgressItem);
        // Return to finish
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        MenuItem composeItem = menu.findItem(R.id.action_compose);

        if (!hasInternet()) {
            Toast.makeText(getApplicationContext(), "No internet", Toast.LENGTH_SHORT).show();
            return false;
        }

        composeItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent i = new Intent(getApplicationContext(), ComposeActivity.class);
                System.out.println("start filter activity");
                startActivityForResult(i, REQUEST_CODE_COMPOSE);
                Log.d("debug", "called startActivityForResult");
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void onProfileView(MenuItem mi) {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    // return order of fragments in view pager
//    public class TweetsPagerAdapter extends FragmentPagerAdapter {
    public class TweetsPagerAdapter extends SmartFragmentStatePagerAdapter {

        private String tabTitles[] = {"Home", "Mentions"};

        // adapter gets manager to insert or remove fragment from activity
        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        // order and creation of fragments within pager
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                HomeTimelineFragment h = new HomeTimelineFragment();
                h.miActionProgressItem = miActionProgressItem;
                return h;
            }
            else if (position == 1) {
                MentionsTimelineFragment m = new MentionsTimelineFragment();
                m.miActionProgressItem = miActionProgressItem;
                return m;
            }
            else {
                return null;
            }
        }

        // return tab title
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        // how many fragments to swipe between
        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_COMPOSE) {
            // refresh timeline
//            TweetsListFragment tf = (TweetsListFragment) tweetsPagerAdapter.getRegisteredFragment(vpPager.getCurrentItem());
//            tf.populateTimeline();

            miActionProgressItem.setVisible(true);

            for (int i=0; i < tweetsPagerAdapter.getCount(); i++) {
                TweetsListFragment tf = (TweetsListFragment) tweetsPagerAdapter.getRegisteredFragment(i);
                tf.hasInter = hasInternet();
                tf.populateGenericTimelineWithSinceId();
            }

            miActionProgressItem.setVisible(false);

//            TweetsListFragment tf = (TweetsListFragment) tweetsPagerAdapter.getRegisteredFragment(0);
//            tf.populateHomeTimelineWithSinceId();
        }
    }


    public Boolean hasInternet() {
        return (isNetworkAvailable() && isOnline());
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }

}
