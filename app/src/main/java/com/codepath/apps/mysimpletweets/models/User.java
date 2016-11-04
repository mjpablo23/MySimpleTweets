package com.codepath.apps.mysimpletweets.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
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
 */
public class User implements Parcelable {

    // list attributes
    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;
    private String backgroundImageUrl;
    private String tagline;
    private int followersCount;
    private int followingsCount;



    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }


    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }

    public String getTagline() {
        return tagline;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingsCount() {
        return followingsCount;
    }

    // deserialize user json -> user
    public static User fromJSON(JSONObject json) {
        User u = new User();
        // extrack and fill values
        try {
            u.name = json.getString("name");
            u.uid = json.getLong("id");
            u.screenName = json.getString("screen_name");
            u.profileImageUrl = json.getString("profile_image_url");
            u.tagline = json.getString("description");
            u.followersCount = json.getInt("followers_count");
            u.followingsCount = json.getInt("friends_count");
            u.backgroundImageUrl = json.getString("profile_background_image_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // return a user
        return u;
    }

    public User() {

    }

    // parcelable methods

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.screenName);
        dest.writeString(this.profileImageUrl);
        dest.writeLong(this.uid);
    }

    protected User(Parcel in) {
        this.name = in.readString();
        this.screenName= in.readString();
        this.profileImageUrl = in.readString();
        this.uid = in.readLong();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
