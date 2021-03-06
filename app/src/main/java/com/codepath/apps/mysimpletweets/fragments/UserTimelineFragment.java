package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;

/**
 * Created by paulyang on 11/2/16.
 */



// on 54:23
public class UserTimelineFragment extends TweetsListFragment {

//    // move client into the TweetsListFragment
//    private TwitterClient client;
//    // for endless scroll
//    private long lowestId_userTimeline = -1;
//    private long sinceId_userTimeline = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        client = TwitterApp.getRestClient();  // singleton client
        super.fragmentType = "user";
        super.screenNameClicked = getArguments().getString("screen_name");
        if (super.screenNameClicked == null) {
            super.screenNameClicked = "";
        }
        super.populateTimeline();
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

//    private void populateUserTimeline() {
//        String screenName = getArguments().getString("screen_name");
//        client.getUserTimeline("user", screenName, new JsonHttpResponseHandler() {
//
//        });
//    }
}
