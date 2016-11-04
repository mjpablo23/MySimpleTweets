package com.codepath.apps.mysimpletweets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.ProfileActivity;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.MentionsTimelineFragment;

public class TimelineActivity extends AppCompatActivity {

    // pull to refresh
    // http://guides.codepath.com/android/Implementing-Pull-to-Refresh-Guide
    private SwipeRefreshLayout swipeContainer;

    // compose activity
    private final int REQUEST_CODE_COMPOSE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get viewpager
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        // set the view pager adapter for the pager
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        // find the sliding tabstrip
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // attach the tabstrip to the view pager
        tabStrip.setViewPager(vpPager);

//        // click on item to show detail view
//        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent i = new Intent(getApplicationContext(), TweetDetailActivity.class);
//                Tweet t = tweets.get(position);
//
//                Log.d("debug", "clicked on tweet: " + t.getUid());
//                i.putExtra("tweet", t);
//                i.putExtra("user", t.getUser());
//                // i.putExtra("tweet", Parcels.wrap(t));
//                startActivity(i);
//            }
//        });

//        // endless scrolling
//        lvTweets.setOnScrollListener(new EndlessScrollListener() {
//            @Override
//            public boolean onLoadMore(int page, int totalItemsCount) {
//                // Triggered only when new data needs to be appended to the list
//                // Add whatever code is needed to append new items to your AdapterView
//                loadNextDataFromApi(lowestId-1);
//                // or loadNextDataFromApi(totalItemsCount);
//                return true; // ONLY if more data is actually being loaded; false otherwise.
//            }
//        });

//        // pull to refresh
//        // Lookup the swipe container view
//        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
//        // Setup refresh listener which triggers new data loading
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                // Your code to refresh the list here.
//                // Make sure you call swipeContainer.setRefreshing(false)
//                // once the network request has completed successfully.
//                fetchTimelineAsync(0);
//            }
//        });
//        // Configure the refreshing colors
//        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_red_light);

    }

//    // for pull to refresh
//    public void fetchTimelineAsync(int page) {
//        client.getHomeTimeline(new JsonHttpResponseHandler() {
//            // success
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
//                Log.d("Debug", json.toString());
//                // deserialize json
//                // create models and add them to adapter
//                // load model data into listview
//
//                fragmentTweetsList.clear();
//
//                ArrayList<Tweet> tweets = Tweet.fromJSONArray(json);
//
//                // https://dev.twitter.com/rest/public/timelines
//                // find lowest id from tweets, as max_id
//                Tweet t = tweets.get(tweets.size()-1);  // last element in ArrayList
//                lowestId = t.getUid();
//                Log.d("debug", "maxId: " + lowestId);
//                fragmentTweetsList.addAll(tweets);
//
//                // Now we call setRefreshing(false) to signal refresh has finished
//                swipeContainer.setRefreshing(false);
//
//            }
//
//            // failure
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                Log.d("Debug", errorResponse.toString());
//            }
//        });
//    }



//    // used for endless scrolling
//    // Append the next page of data into the adapter
//    // This method probably sends out a network request and appends new data items to your adapter.
//    public void loadNextDataFromApi(long offset) {
//        // Send an API request to retrieve appropriate paginated data
//        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
//        //  --> Deserialize and construct new model objects from the API response
//        //  --> Append the new data objects to the existing set of items inside the array of items
//        //  --> Notify the adapter of the new items made with `notifyDataSetChanged()`
//
//        client.getHomeTimeline(new JsonHttpResponseHandler() {
//            // success
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
//                Log.d("Debug", json.toString());
//                // deserialize json
//                // create models and add them to adapter
//                // load model data into listview
//                ArrayList<Tweet> tweets = Tweet.fromJSONArray(json);
//
//                // find lowest id from tweets, as max_id
//                Tweet t = tweets.get(tweets.size()-1);  // last element in ArrayList
//                lowestId = t.getUid();
//                sinceId = tweets.get(0).getUid();
//                Log.d("debug", "maxId: " + lowestId);
//                fragmentTweetsList.addAll(tweets);
//            }
//
//            // failure
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                Log.d("Debug", errorResponse.toString());
//            }
//            // this call has lowestId as second argument, subtract 1 as described in documentation
//        }, offset, 1);
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        MenuItem composeItem = menu.findItem(R.id.action_compose);
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
    public class TweetsPagerAdapter extends FragmentPagerAdapter {

        private String tabTitles[] = {"Home", "Mentions"};

        // adapter gets manager to insert or remove fragment from activity
        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        // order and creation of fragments within pager
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new HomeTimelineFragment();
            }
            else if (position == 1) {
                return new MentionsTimelineFragment();
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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_COMPOSE) {
//            // refresh timeline
//            populateTimeline();
//
//        }
//    }
}
