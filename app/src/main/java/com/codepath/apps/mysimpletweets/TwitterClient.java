package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */

// readme for rest client template from codepath: https://github.com/codepath/android-rest-client-template
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "C7Q05VHCCfNxezVBcN4BpnRFW";       // Change this
	public static final String REST_CONSUMER_SECRET = "QzqQfpRxiEGBfp79nrznEEadlmbNBbsuUdugLAFEJI9wKvmdOX"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets"; // Change this (here and in manifest)

    private int numTweetsPerCall = 10;

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

    public String getApiUrlForType(String type) {
        String apiUrl = "";
        if (type.equals("home")) {
            apiUrl = getApiUrl("statuses/home_timeline.json");
        }
        else if (type.equals("mentions")) {
            apiUrl = getApiUrl("statuses/mentions_timeline.json");
        }
        else if (type.equals("user")) {
            apiUrl = getApiUrl("statuses/user_timeline.json");
        }
        else {
            Log.d("debug", "error in getApiUrlForType");
        }
        return apiUrl;
    }

	// method for endpoint
//	GET statuses/home_timeline.json
	public void getHomeTimeline(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		// specify params
		RequestParams params = new RequestParams();
		params.put("count", numTweetsPerCall);
		// execute request
		getClient().get(apiUrl, params, handler);
	}

    // alternate getHomeTimeline function with parameter for max_id
    public void getHomeTimelineWithLowestId(long lowestId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        // specify params
        RequestParams params = new RequestParams();
        params.put("count", numTweetsPerCall);
        params.put("max_id", lowestId);
		// params.put("since_id", sinceId);

        // execute request
        getClient().get(apiUrl, params, handler);
    }

//    // alternate getHomeTimeline function with parameter for max_id
//    public void getHomeTimelineWithSinceId(long sinceId, AsyncHttpResponseHandler handler) {
//        String apiUrl = getApiUrl("statuses/home_timeline.json");
//        // specify params
//        RequestParams params = new RequestParams();
//        params.put("count", 3);
////        params.put("max_id", lowestId);
//         params.put("since_id", sinceId);
//
//        // execute request
//        getClient().get(apiUrl, params, handler);
//    }

    public void getGenericTimeline(String type, AsyncHttpResponseHandler handler) {
        // String apiUrl = getApiUrl("statuses/home_timeline.json");
        String apiUrl = getApiUrlForType(type);
        // specify params
        RequestParams params = new RequestParams();
        params.put("count", numTweetsPerCall);
        // execute request
        getClient().get(apiUrl, params, handler);
    }

    // alternate getHomeTimeline function with parameter for max_id
    public void getGenericTimelineWithLowestId(String type, long lowestId, AsyncHttpResponseHandler handler) {
        // String apiUrl = getApiUrl("statuses/home_timeline.json");
        String apiUrl = getApiUrlForType(type);
                // specify params
        RequestParams params = new RequestParams();
        params.put("count", numTweetsPerCall);
        params.put("max_id", lowestId);
        // params.put("since_id", sinceId);

        // execute request
        getClient().get(apiUrl, params, handler);
    }

    public void getGenericTimelineWithSinceId(String type, long sinceId, AsyncHttpResponseHandler handler) {
        // String apiUrl = getApiUrl("statuses/home_timeline.json");
        // specify params
        String apiUrl = getApiUrlForType(type);

        RequestParams params = new RequestParams();
        params.put("count", 3);
        params.put("since_id", sinceId);

        // execute request
        getClient().get(apiUrl, params, handler);
    }

    public void makePost(AsyncHttpResponseHandler handler, String postStr) {
        String apiUrl = getApiUrl("statuses/update.json");

        RequestParams params = new RequestParams();
        params.put("status", postStr);

        // http://stackoverflow.com/questions/107390/whats-the-difference-between-a-post-and-a-put-http-request
        // call post, not put?
        getClient().post(apiUrl, params, handler);
    }

	public void getMentionsTimeline(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		// specify params
		RequestParams params = new RequestParams();
		params.put("count", numTweetsPerCall);
		params.put("since_id", 1);

		// execute request
		getClient().get(apiUrl, params, handler);
	}

    public void getUserTimeline(AsyncHttpResponseHandler handler) {
        getUserTimeline("", handler);
    }

    public void getUserTimeline(String screenName, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        // specify params
        RequestParams params = new RequestParams();
        params.put("count", numTweetsPerCall);
        if (screenName != null && !screenName.isEmpty()) {
            params.put("screen_name", screenName);
        }
        getClient().get(apiUrl, params, handler);
    }

    public void getUserInfo(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        getClient().get(apiUrl, null, handler);
    }

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}