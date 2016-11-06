package com.codepath.apps.mysimpletweets;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.fragments.UserTimelineFragment;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {
    TwitterClient client;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        client = TwitterApp.getRestClient();

        final String screenName = getIntent().getStringExtra("screen_name");

        // original code
        if (screenName == null) {
            client.getUserInfoForSelf(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                              user = User.fromJSON(response);
                                              String userName = user.getScreenName();
                                              Log.d("debug", "screen_name: " + screenName + "userName: " + userName);
                                              setTitle(userName);

                    populateProfileHeader(user);
                }
            });
        }
        else {
            client.getUserInfo(screenName, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = User.fromJSON(response);
                    String userName = user.getScreenName();
                    Log.d("debug", "screen_name: " + screenName + "userName: " + userName);
                    setTitle(userName);

                    populateProfileHeader(user);
                }
            });
        }
        // get screen name from activity that laucnhes this
        // String screenName = getIntent().getStringExtra("screen_name");

        if (savedInstanceState == null) {
            // create the user timeline fragment
            UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(screenName);

            // display user fragment within this activity (dynamic)
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragmentUserTimeline);
            ft.commit();

            // 1:04:31
        }
    }

    private void populateProfileHeader(User user) {
        TextView tvName = (TextView) findViewById(R.id.tvFullName);
        TextView tvTagLine = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvName.setText(user.getName());
        tvTagLine.setText(user.getTagline());
        int followers = user.getFollowersCount();
        int following = user.getFollowingsCount();
        Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);
        tvFollowers.setText(followers + " followers");
        tvFollowing.setText(following + " following");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

}
