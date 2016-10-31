package com.codepath.apps.mysimpletweets.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.codepath.apps.mysimpletweets.R;

/**
 * Created by paulyang on 10/29/16.
 */
public class TweetDetailActivity extends AppCompatActivity {

    // @BindView(R.id.bodyTextView) TextView body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tweet_detailed_view);
        // ButterKnife.bind(this);

        // Tweet t = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweet"));

        // body.setText(t.getBody());


    }
}
