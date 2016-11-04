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
public class MentionsTimelineFragment extends TweetsListFragment {
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

    // send api request to get timeline json
    // fill listview by creating tweet objects from json
    private void populateTimeline() {
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
