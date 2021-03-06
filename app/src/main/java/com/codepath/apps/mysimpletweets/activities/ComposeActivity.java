package com.codepath.apps.mysimpletweets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApp;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by paulyang on 10/29/16.
 */
public class ComposeActivity extends AppCompatActivity {

    private TwitterClient client;
    private EditText tweetText;
    private TextView charsLeft;

    int numCharsLeft = 140;

    boolean isReply = false;
    String screenName = null;
    String uid = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        tweetText = (EditText) findViewById(R.id.editTextCompose);
        charsLeft = (TextView) findViewById(R.id.textViewCharactersRemaining);

        screenName = getIntent().getStringExtra("screen_name");
        uid = getIntent().getStringExtra("uid");

        if (screenName != null) {
            isReply = true;
            tweetText.setText("@" + screenName);
        }

        // listener for editText
        tweetText.addTextChangedListener(mTextEditorWatcher);

        client = TwitterApp.getRestClient();  // singleton client


    }

    // post to timeline using twitter client's put method
    private void postToTimeline(String tweet) {
        if (!isReply) {
            client.makePost(new JsonHttpResponseHandler() {
                // need to handle json response or not?


                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.d("Debug", response.toString());
                    finish();
                }

                // failure
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("Debug", errorResponse.toString());
                    Toast.makeText(getApplicationContext(), "error posting", Toast.LENGTH_LONG).show();
                    finish();
                }

            }, tweet);
        }
        else {
            replyToPost(tweet, uid);
        }
    }

    private void replyToPost(String tweet, String replyToStatusId) {
        client.makeReply(tweet, replyToStatusId, new JsonHttpResponseHandler() {
            // need to handle json response or not?

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("Debug", response.toString());
                finish();
            }

            // failure
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("Debug", errorResponse.toString());
                Toast.makeText(getApplicationContext(), "error posting", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    // Post button press
    public void postClickHandler(View target) {
        // Do stuff
        // get text from edit view
        String tweet = tweetText.getText().toString();
        Log.d("debug", tweet);

        if (numCharsLeft < 0) {
            Toast.makeText(getApplicationContext(), "too many characters", Toast.LENGTH_LONG).show();
            return;
        }

        postToTimeline(tweet);

        Intent data = new Intent();
//        data.putExtra("checkArts", checkArts);
        setResult(RESULT_OK, data);


    }

    // character count for edittext
    // http://stackoverflow.com/questions/3013791/live-character-count-for-edittext
    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            numCharsLeft = 140 - s.length();
            charsLeft.setText("characters left: " + String.valueOf(numCharsLeft));
        }

        public void afterTextChanged(Editable s) {
        }
    };
}
