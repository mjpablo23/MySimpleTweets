package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;

/**
 * Created by paulyang on 11/2/16.
 */
public class HomeTimelineFragment extends TweetsListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.fragmentType = "home";
        super.populateTimeline();
    }


}
