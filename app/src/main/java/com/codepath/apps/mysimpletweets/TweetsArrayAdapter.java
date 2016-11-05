package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

// take tweet objects and turn them into views displayed in list
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    // override and setup custom template
    // Viewholder pattern

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1. get the tweet
        final Tweet tweet = getItem(position);
        // 2. find or inflate the template
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }
        // 3. find the subviews to fill with data in the template
        ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);

        // LinkifiedTextView tvBody = (LinkifiedTextView) convertView.findViewById(R.id.tvBody);
        TextView tvName = (TextView) convertView.findViewById(R.id.name);
        TextView timestamp = (TextView) convertView.findViewById(R.id.relativeTimestamp);

        // 4. populate data into the subviews
        tvUserName.setText("");
        tvUserName.setText("@" + tweet.getUser().getScreenName());
        tvName.setText(tweet.getUser().getName());
        //timestamp.setText(getRelativeTime(tweet.getCreatedAt()));
        timestamp.setText(tweet.getRelativeTime());

        // changed to linkifiedTextView
        tvBody.setText(tweet.getBody());
        // tvBody.setText(Html.fromHtml(tweet.getBody()), TextView.BufferType.SPANNABLE);

        ivProfileImage.setImageResource(android.R.color.transparent);  // clear out the old image for a recycled view
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);

        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoProfileForUser(tweet.getUser());
            }
        });

        // 5. return the view to be inserted into the list
        return convertView;
    }

    public void gotoProfileForUser(User u) {
        Log.d("debug", "clicked on image for user: " + u.getName());
    }

//    public void onProfileView(MenuItem mi) {
//        Intent i = new Intent(this, ProfileActivity.class);
//        startActivity(i);
//    }

}
