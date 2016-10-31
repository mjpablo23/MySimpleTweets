package com.codepath.apps.mysimpletweets.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by paulyang on 10/29/16.
 */
public class TweetDetailActivity extends AppCompatActivity {

    @BindView(R.id.bodyTextView) TextView body;
    @BindView(R.id.screenNameTextView) TextView screenname;
    @BindView(R.id.nameText) TextView name;
    @BindView(R.id.detailTimestamp) TextView timestamp;
    @BindView(R.id.detailImageView) ImageView ivProfileImage;

    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tweet_detailed_view);
        ButterKnife.bind(this);

        Tweet t = (Tweet) getIntent().getParcelableExtra("tweet");
        User u = (User) getIntent().getParcelableExtra("user");
        // Tweet t = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweet"));



        name.setText(u.getName());
        screenname.setText("@" + u.getScreenName());
        body.setText(t.getBody());
        timestamp.setText(t.getRelativeTime());

        context = getApplicationContext();

        // ivProfileImage.setImageResource(android.R.color.transparent);  // clear out the old image for a recycled view
        String url = u.getProfileImageUrl();
        Log.d("debug", url);
        Picasso.with(context).load(url).into(ivProfileImage);
    }
}
