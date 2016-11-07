package com.codepath.apps.mysimpletweets.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.codepath.apps.mysimpletweets.ProfileActivity;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.TwitterApp;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.activities.ComposeActivity;
import com.codepath.apps.mysimpletweets.activities.TweetDetailActivity;
import com.codepath.apps.mysimpletweets.custom.EndlessScrollListener;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by paulyang on 11/2/16.
 */
public class TweetsListFragment extends Fragment {

    //public Context parentContext;
    public Boolean hasInter = true;

    public ArrayList<Tweet> tweets;
    public TweetsArrayAdapter aTweets;
    public ListView lvTweets;

    public String fragmentType = "";

    // pull to refresh
    // http://guides.codepath.com/android/Implementing-Pull-to-Refresh-Guide
    private SwipeRefreshLayout swipeContainer;

    // moved here from HomeTimelineFragment and UserTimelineFragment
    private TwitterClient client;

    // home timeline
    private long lowestId = -1;
    private long sinceId = 1;

    public String screenNameClicked = "";

    private final int REQUEST_CODE_COMPOSE = 20;

    // progress bar
    public MenuItem miActionProgressItem;

    // inflation logic
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, parent, false);
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        // connect adapter to list view
        lvTweets.setAdapter(aTweets);

        // click on item to show detail view
        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getContext(), TweetDetailActivity.class);
                Tweet t = tweets.get(position);

                Log.d("debug", "clicked on tweet: " + t.getUid());
                i.putExtra("tweet", t);
                i.putExtra("user", t.getUser());
                // i.putExtra("tweet", Parcels.wrap(t));
                startActivity(i);
            }
        });

        startPullToRefreshListener(v);

        startEndlessScrollListener();

        return v;
    }

    // creation lifecycle

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApp.getRestClient();  // singleton client
        // create arraylist (data source)
        tweets = new ArrayList<>();
        // construct adapter from data souce
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);

        setItemListenerInAdapter();
    }

    public void setItemListenerInAdapter() {
        aTweets.setProfileImageClickedListener(new TweetsArrayAdapter.ProfileImageClickedListener() {
            @Override
            public void onObjectReady(String title) {

            }

            @Override
            public void onDataLoaded(String screenName) {
                Log.d("debug", "TweetsListFragment sees image clicked for: " + screenName);
                screenNameClicked = screenName;
                Intent i = new Intent(getActivity(), ProfileActivity.class);
                i.putExtra("screen_name", screenName);
                startActivity(i);
            }

            public void reply(String uid, String screenName) {
                Intent i = new Intent(getActivity(), ComposeActivity.class);
                System.out.println("start filter activity");
                i.putExtra("uid", uid);
                i.putExtra("screen_name", screenName);
                startActivityForResult(i, REQUEST_CODE_COMPOSE);
                Log.d("debug", "called startActivityForResult");
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        // Extract the action-view from the menu item
        ProgressBar v =  (ProgressBar) MenuItemCompat.getActionView(miActionProgressItem);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    // ----------- endless scrolling ------------
    public void startEndlessScrollListener() {
                // endless scrolling
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                loadNextDataForEndlessScroll(lowestId-1);
                // or loadNextDataForEndlessScroll(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });
    }

    // used for endless scrolling
    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataForEndlessScroll(long offset) {

        if (!hasInternet()) {
            return;
        }

        showProgressBar();
        if (screenNameClicked.isEmpty()) {

            client.getGenericTimelineForEndlessScroll(fragmentType, offset, new JsonHttpResponseHandler() {
                // success
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                    Log.d("Debug", json.toString());
                    // deserialize json
                    // create models and add them to adapter
                    // load model data into listview
                    ArrayList<Tweet> tweets = Tweet.fromJSONArray(json);

                    // find lowest id from tweets, as max_id
                    if (tweets.size() > 0) {
                        Tweet t = tweets.get(tweets.size() - 1);  // last element in ArrayList
                        lowestId = t.getUid();
                        // sinceId = tweets.get(0).getUid();
                        Log.d("debug", "maxId: " + lowestId);
                        aTweets.addAll(tweets);
                    }
                }

                // failure

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("Debug", errorResponse.toString());
                }
                // this call has lowestId as second argument, subtract 1 as described in documentation
            });
        }
        else {
            client.getUserTimelineForEndlessScroll(fragmentType, screenNameClicked, offset, new JsonHttpResponseHandler() {
                // success
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                    Log.d("Debug", json.toString());
                    // deserialize json
                    // create models and add them to adapter
                    // load model data into listview
                    ArrayList<Tweet> tweets = Tweet.fromJSONArray(json);

                    // find lowest id from tweets, as max_id
                    if (tweets.size() > 0) {
                        Tweet t = tweets.get(tweets.size() - 1);  // last element in ArrayList
                        lowestId = t.getUid();
                        // sinceId = tweets.get(0).getUid();
                        Log.d("debug", "maxId: " + lowestId);
                        aTweets.addAll(tweets);
                    }
                }

                // failure

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("Debug", errorResponse.toString());
                }
                // this call has lowestId as second argument, subtract 1 as described in documentation
            });
        }
        hideProgressBar();
    }


    // ---------- pull to refresh ------------------
    public void startPullToRefreshListener(View v) {
                // pull to refresh
        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }


    public void addAll(List<Tweet> tweets) {
        aTweets.addAll(tweets);
    }

    public void clear() {
        aTweets.clear();
    }


    public void populateTimeline() {
//        if (!hasInternet()) {
//            return;
//        }

        showProgressBar();

        // at 45:23 in video
        if (screenNameClicked.isEmpty()) {
            client.getGenericTimeline(fragmentType, new JsonHttpResponseHandler() {
                // success

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                    Log.d("Debug", json.toString());
                    // deserialize json
                    // create models and add them to adapter
                    // load model data into listview

                    clear();

                    ArrayList<Tweet> tweets = Tweet.fromJSONArray(json);
                    if (tweets.size() > 0) {
                        // https://dev.twitter.com/rest/public/timelines
                        // find lowest id from tweets, as max_id
                        Tweet t = tweets.get(tweets.size() - 1);  // last element in ArrayList
                        lowestId = t.getUid();
                        sinceId = tweets.get(1).getUid();
                        Log.d("debug", "maxId: " + lowestId);

                        addAll(tweets);
                    }
                    lvTweets.smoothScrollToPosition(0);
                }

                // failure

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    if (errorResponse != null) {
                        Log.d("Debug", errorResponse.toString());
                    }
                }
            });
        }
        else {
            client.getUserTimeline(fragmentType, screenNameClicked, new JsonHttpResponseHandler() {
                // success

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                    Log.d("Debug", json.toString());
                    // deserialize json
                    // create models and add them to adapter
                    // load model data into listview

                    clear();

                    ArrayList<Tweet> tweets = Tweet.fromJSONArray(json);
                    if (tweets.size() > 0) {
                        // https://dev.twitter.com/rest/public/timelines
                        // find lowest id from tweets, as max_id
                        Tweet t = tweets.get(tweets.size() - 1);  // last element in ArrayList
                        lowestId = t.getUid();
                        sinceId = tweets.get(1).getUid();
                        Log.d("debug", "maxId: " + lowestId);

                        addAll(tweets);
                    }
                    lvTweets.smoothScrollToPosition(0);
                }

                // failure

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    if (errorResponse != null) {
                        Log.d("Debug", errorResponse.toString());
                    }
                }
            });
        }

        hideProgressBar();
    }

    public void populateGenericTimelineWithSinceId() {
        // at 45:23 in video

        if (!hasInternet()) {
            return;
        }

        if(screenNameClicked.isEmpty()) {
            client.getGenericTimelineForRefresh(fragmentType, sinceId, new JsonHttpResponseHandler() {
                // success
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                    Log.d("Debug", json.toString());
                    // deserialize json
                    showNewestTweets(json);
                }

                // failure
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("Debug", errorResponse.toString());
                }
            });
        }
        else {
            client.getUserTimelineForRefresh(fragmentType, screenNameClicked, sinceId, new JsonHttpResponseHandler() {
                // success
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                    Log.d("Debug", json.toString());
                    // deserialize json
                    showNewestTweets(json);
                }

                // failure
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("Debug", errorResponse.toString());
                }
            });
        }
    }

    public void showNewestTweets(JSONArray json) {
        ArrayList<Tweet> tweets = Tweet.fromJSONArray(json);

        // https://dev.twitter.com/rest/public/timelines
        // find lowest id from tweets, as max_id
        Log.d("debug", "num tweets retrieved: " + tweets.size());
        if (tweets.size() > 0) {
            Tweet t = tweets.get(0);  // last element in ArrayList
//                lowestId = t.getUid();
            sinceId = tweets.get(0).getUid();
            Log.d("debug", "maxId: " + lowestId);

            // addAll(tweets);
            aTweets.insert(t, 0);
            aTweets.notifyDataSetChanged();
        }
        lvTweets.smoothScrollToPosition(0);
    }



    // for pull to refresh
    protected void fetchTimelineAsync(int page) {

        if (!hasInternet()) {
            return;
        }

        populateTimeline();
        swipeContainer.setRefreshing(false);
    }

    // progress bar
    public void showProgressBar() {
        // Show progress item
        Log.d("debug", "showProgressBar");
        if (miActionProgressItem != null) {
            miActionProgressItem.setVisible(true);
        }
        else {
            Log.d("debug", "progressBar null");
        }
    }

    public void hideProgressBar() {
        // Hide progress item
        Log.d("debug", "hideProgressBar");
        if (miActionProgressItem != null) {
            miActionProgressItem.setVisible(false);
        }
        else {
            Log.d("debug", "progressBar null");
        }
    }

    public Boolean hasInternet() {
        return hasInter;
    }
//
//    private Boolean isNetworkAvailable() {
//        ConnectivityManager connectivityManager
//                = (ConnectivityManager) parentContext.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
//    }
//
//    public boolean isOnline() {
//        Runtime runtime = Runtime.getRuntime();
//        try {
//            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
//            int     exitValue = ipProcess.waitFor();
//            return (exitValue == 0);
//        } catch (IOException e)          { e.printStackTrace(); }
//        catch (InterruptedException e) { e.printStackTrace(); }
//        return false;
//    }

}
