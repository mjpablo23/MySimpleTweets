package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.Tweet;

import java.util.List;

/**
 * Created by paulyang on 10/30/16.
 */
public class TweetsRecyclerAdapter extends RecyclerView.Adapter<TweetsRecyclerAdapter.ViewHolder> {

    List<Tweet> tweets;
    Context context;

    public TweetsRecyclerAdapter(Context context, List<Tweet> tweets) {
        this.tweets = tweets;
        this.context = context;
        // super(context, android.R.layout.simple_list_item_1, tweets);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView bodyTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            bodyTextView = (TextView) itemView.findViewById(R.id.tvBody);
        }
    }

    // http://stackoverflow.com/questions/27450598/android-widget-textview-cannot-be-applied-to-android-view-view
    @Override
    public TweetsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);

        // View convertView = inflater.from(context).inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(TweetsRecyclerAdapter.ViewHolder viewHolder, int position) {
        Tweet t = tweets.get(position);
        TextView body = viewHolder.bodyTextView;
        body.setText(t.getBody());
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    private Context getContext() {
        return context;
    }

    // override and setup custom template
    // Viewholder pattern

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        // 1. get the tweet
//        Tweet tweet = getItem(position);
//        // 2. find or inflate the template
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
//        }
//        // 3. find the subviews to fill with data in the template
//        ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
//        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
//        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
//
//        // LinkifiedTextView tvBody = (LinkifiedTextView) convertView.findViewById(R.id.tvBody);
//        TextView tvName = (TextView) convertView.findViewById(R.id.name);
//        TextView timestamp = (TextView) convertView.findViewById(R.id.relativeTimestamp);
//
//        // 4. populate data into the subviews
//        tvUserName.setText("");
//        tvUserName.setText("@" + tweet.getUser().getScreenName());
//        tvName.setText(tweet.getUser().getName());
//        //timestamp.setText(getRelativeTime(tweet.getCreatedAt()));
//        timestamp.setText(tweet.getRelativeTime());
//
//        // changed to linkifiedTextView
//        tvBody.setText(tweet.getBody());
//        // tvBody.setText(Html.fromHtml(tweet.getBody()), TextView.BufferType.SPANNABLE);
//
//        ivProfileImage.setImageResource(android.R.color.transparent);  // clear out the old image for a recycled view
//        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);
//        // 5. return the view to be inserted into the list
//        return convertView;
//    }

}
