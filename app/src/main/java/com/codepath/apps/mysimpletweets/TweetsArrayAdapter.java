package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;

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
        Tweet tweet = getItem(position);
        // 2. find or inflate the template
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }
        // 3. find the subviews to fill with data in the template
        ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        TextView tvName = (TextView) convertView.findViewById(R.id.name);
        TextView timestamp = (TextView) convertView.findViewById(R.id.relativeTimestamp);

        // 4. populate data into the subviews
        tvUserName.setText("");
        tvUserName.setText("@" + tweet.getUser().getScreenName());
        tvName.setText(tweet.getUser().getName());
        timestamp.setText(getRelativeTime(tweet.getCreatedAt()));
        tvBody.setText(tweet.getBody());
        ivProfileImage.setImageResource(android.R.color.transparent);  // clear out the old image for a recycled view
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);
        // 5. return the view to be inserted into the list
        return convertView;
    }

    public String getRelativeTime(String createdAt) {
        // http://stackoverflow.com/questions/25350164/java-parsing-twitters-created-at-string
        System.out.println("created_at: " + createdAt);
        final String TWITTER = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(TWITTER);
        Date date = new Date();
        try {
            date = sf.parse(createdAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // long now = System.currentTimeMillis();
        long past = date.getTime();
        // DateUtils.getRelativeDateTimeString(, now, 0L, DateUtils.FORMAT_ABBREV_ALL);
        String pastStr = DateUtils.getRelativeTimeSpanString(past).toString();
        String[] arr = pastStr.split("\\s+");

        String retStr = "";
        if (arr.length > 1) {
            String unit = arr[1].substring(0, 1);
            retStr = arr[0] + "" + unit;
        }
        else {
            retStr = arr[0];
        }
        return retStr;
    }
}
