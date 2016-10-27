package com.codepath.apps.mysimpletweets.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 {
 "coordinates": null,
 "truncated": false,
 "created_at": "Tue Aug 28 19:59:34 +0000 2012",
 "favorited": false,
 "id_str": "240539141056638977",
 "in_reply_to_user_id_str": null,
 "entities": {
 "urls": [

 ],
 "hashtags": [

 ],
 "user_mentions": [

 ]
 },
 "text": "You'd be right more often if you thought you were wrong.",
 "contributors": null,
 "id": 240539141056638977,
 "retweet_count": 1,
 "in_reply_to_status_id_str": null,
 "geo": null,
 "retweeted": false,
 "in_reply_to_user_id": null,
 "place": null,
 "source": "web",
 "user": {
 "name": "Taylor Singletary",
 "profile_sidebar_fill_color": "FBFBFB",
 "profile_background_tile": true,
 "profile_sidebar_border_color": "000000",
 "profile_image_url": "http://a0.twimg.com/profile_images/2546730059/f6a8zq58mg1hn0ha8vie_normal.jpeg",
 "created_at": "Wed Mar 07 22:23:19 +0000 2007",
 "location": "San Francisco, CA",
 "follow_request_sent": false,
 "id_str": "819797",
 "is_translator": false,
 "profile_link_color": "c71818",
 "entities": {
 "url": {
 "urls": [
 {
 "expanded_url": "http://www.rebelmouse.com/episod/",
 "url": "http://t.co/Lxw7upbN",
 "indices": [
 0,
 20
 ],
 "display_url": "rebelmouse.com/episod/"
 }
 ]
 },
 "description": {
 "urls": [

 ]
 }
 },
 "default_profile": false,
 "url": "http://t.co/Lxw7upbN",
 "contributors_enabled": false,
 "favourites_count": 15990,
 "utc_offset": -28800,
 "profile_image_url_https": "https://si0.twimg.com/profile_images/2546730059/f6a8zq58mg1hn0ha8vie_normal.jpeg",
 "id": 819797,
 "listed_count": 340,
 "profile_use_background_image": true,
 "profile_text_color": "D20909",
 "followers_count": 7126,
 "lang": "en",
 "protected": false,
 "geo_enabled": true,
 "notifications": false,
 "description": "Reality Technician, Twitter API team, synthesizer enthusiast; a most excellent adventure in timelines. I know it's hard to believe in something you can't see.",
 "profile_background_color": "000000",
 "verified": false,
 "time_zone": "Pacific Time (US & Canada)",
 "profile_background_image_url_https": "https://si0.twimg.com/profile_background_images/643655842/hzfv12wini4q60zzrthg.png",
 "statuses_count": 18076,
 "profile_background_image_url": "http://a0.twimg.com/profile_background_images/643655842/hzfv12wini4q60zzrthg.png",
 "default_profile_image": false,
 "friends_count": 5444,
 "following": true,
 "show_all_inline_media": true,
 "screen_name": "episod"
 },
 "in_reply_to_screen_name": null,
 "in_reply_to_status_id": null
 }
 */

// parse the json, store the data, encapsulate logic or display logic
public class Tweet {
    // list of attributes
    private String body;
    private long uid;  // unique id for tweet
    private User user;
    private String createdAt;

    public String getBody() {
        return body;
    }

    public User getUser() {
        return user;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    // deserialize json and build tweet objects
    // tweet.fromJSON("{...}") => <Tweet>
    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        // extract values from json, store them
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // return tweet object
        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();
        for (int i=0; i<jsonArray.length(); i++) {
            try {
                JSONObject tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJson);
                if (tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }

        return tweets;
    }
}
