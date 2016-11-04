package com.codepath.apps.mysimpletweets.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.TwitterApp;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.activities.TweetDetailActivity;
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


//    // mentions timeline
//    private long lowestId_mentionsTimeline = -1;
//    private long sinceId_mentionsTimeline = 1;
//
//    // users timeline
//    private long lowestId_userTimeline = -1;
//    private long sinceId_userTimeline = 1;


    private final int REQUEST_CODE_COMPOSE = 20;

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
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

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

    public void  populateTimeline() {
        if (fragmentType.equals("home")) {
            populateHomeTimeline();
        }
        else if (fragmentType.equals("mentions")) {
            populateMentionsTimeline();
        }
        else if (fragmentType.equals("user")) {
            populateUserTimeline();
        }
        else {
            Log.d("debug", "populateTimeline error");
        }
    }

    // send api request to get timeline json
    // fill listview by creating tweet objects from json
    public void populateHomeTimeline() {
        // at 45:23 in video
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            // success

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Log.d("Debug", json.toString());
                // deserialize json
                // create models and add them to adapter
                // load model data into listview

                clear();

                ArrayList<Tweet> tweets = Tweet.fromJSONArray(json);

                // https://dev.twitter.com/rest/public/timelines
                // find lowest id from tweets, as max_id
                Tweet t = tweets.get(tweets.size()-1);  // last element in ArrayList
                lowestId = t.getUid();
                sinceId = tweets.get(0).getUid();
                Log.d("debug", "maxId: " + lowestId);

                addAll(tweets);

                lvTweets.smoothScrollToPosition(0);
            }

            // failure

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("Debug", errorResponse.toString());
            }
        });
    }

    // send api request to get timeline json
    // fill listview by creating tweet objects from json
    public void populateMentionsTimeline() {
        // at 45:23 in video
        client.getMentionsTimeline(new JsonHttpResponseHandler() {
            // success

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Log.d("Debug", json.toString());
                // deserialize json
                // create models and add them to adapter
                // load model data into listview

                clear();

                ArrayList<Tweet> tweets = Tweet.fromJSONArray(json);

                // https://dev.twitter.com/rest/public/timelines
                // find lowest id from tweets, as max_id
                Tweet t = tweets.get(tweets.size()-1);  // last element in ArrayList
                lowestId = t.getUid();
                sinceId = tweets.get(0).getUid();

                addAll(tweets);

                lvTweets.smoothScrollToPosition(0);
            }

            // failure

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("Debug", errorResponse.toString());
            }
        });
    }

    // move calls to populate timeline into the TweetsListFragment
    // send api request to get timeline json
    // fill listview by creating tweet objects from json
    protected void populateUserTimeline() {
        String screenName = getArguments().getString("screen_name");
        // at 45:23 in video
        client.getUserTimeline(screenName, new JsonHttpResponseHandler() {
            // success

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Log.d("Debug", json.toString());
                // deserialize json
                // create models and add them to adapter
                // load model data into listview

                clear();

                ArrayList<Tweet> tweets = Tweet.fromJSONArray(json);

                // https://dev.twitter.com/rest/public/timelines
                // find lowest id from tweets, as max_id
                Tweet t = tweets.get(tweets.size()-1);  // last element in ArrayList
                lowestId = t.getUid();
                sinceId = tweets.get(0).getUid();

                addAll(tweets);

                lvTweets.smoothScrollToPosition(0);
            }

            // failure

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("Debug", errorResponse.toString());
            }
        });
    }

    // for pull to refresh
    protected void fetchTimelineAsync(int page) {

        populateTimeline();
        swipeContainer.setRefreshing(false);
    }

    //    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_COMPOSE) {
            // refresh timeline
            populateTimeline();

        }
    }
}
