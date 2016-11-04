package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.mysimpletweets.TwitterApp;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by paulyang on 11/2/16.
 */

// on 54:23
public class UserTimelineFragment extends TweetsListFragment {
    private TwitterClient client;
    // for endless scroll
    private long lowestId = -1;
    private long sinceId = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApp.getRestClient();  // singleton client
        populateTimeline();
    }

    // Creates a new fragment given an int and title
    // UserTimelineFragment.newInstance("bob");
    public static UserTimelineFragment newInstance(String screen_name) {
        UserTimelineFragment userFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screen_name);
        userFragment.setArguments(args);
        return userFragment;
    }

    // send api request to get timeline json
    // fill listview by creating tweet objects from json
    private void populateTimeline() {
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
                Log.d("debug", "maxId: " + lowestId);

                addAll(tweets);

                // lvTweets.smoothScrollToPosition(0);
            }

            // failure

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("Debug", errorResponse.toString());
            }
        });
    }
}
