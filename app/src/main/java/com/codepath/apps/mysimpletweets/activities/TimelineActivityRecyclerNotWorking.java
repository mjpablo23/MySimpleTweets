package com.codepath.apps.mysimpletweets.activities;

//
//public class TimelineActivityRecyclerNotWorking extends AppCompatActivity {
//
//    // pull to refresh
//    // http://guides.codepath.com/android/Implementing-Pull-to-Refresh-Guide
//    private SwipeRefreshLayout swipeContainer;
//
//
//    private TwitterClient client;
//    private ArrayList<Tweet> tweets;
//
////     private TweetsArrayAdapter aTweets;
////     private ListView lvTweets;
//
//    private TweetsRecyclerAdapter aTweets;
//    private RecyclerView lvTweets;
//
//    // for endless scroll
//    private long lowestId = -1;
//
//    // compose activity
//    private final int REQUEST_CODE_COMPOSE = 20;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_timeline);
//
//        // find the listview
//        lvTweets = (RecyclerView) findViewById(R.id.lvTweets);
//        // create arraylist (data source)
//        tweets = new ArrayList<>();
//        // construct adapter from data souce
//        aTweets = new TweetsRecyclerAdapter(this, tweets);
//        // connect adapter to list view
//        lvTweets.setAdapter(aTweets);
//
//        lvTweets.setLayoutManager(new LinearLayoutManager(this));
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        client = TwitterApp.getRestClient();  // singleton client
//        populateTimeline();
//
////        // click on item to show detail view
////        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////            @Override
////            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                Intent i = new Intent(getApplicationContext(), TweetDetailActivity.class);
////                Tweet t = tweets.get(position);
////
////                Log.d("debug", "clicked on tweet: " + t.getUid());
////                i.putExtra("tweet", t);
////                i.putExtra("user", t.getUser());
////                // i.putExtra("tweet", Parcels.wrap(t));
////                startActivity(i);
////            }
////        });
//
////        // endless scrolling
////        lvTweets.setOnScrollListener(new EndlessScrollListener() {
////            @Override
////            public boolean onLoadMore(int page, int totalItemsCount) {
////                // Triggered only when new data needs to be appended to the list
////                // Add whatever code is needed to append new items to your AdapterView
////                loadNextDataFromApi(page);
////                // or loadNextDataFromApi(totalItemsCount);
////                return true; // ONLY if more data is actually being loaded; false otherwise.
////            }
////        });
//
//        // pull to refresh
//        // Lookup the swipe container view
//        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
//        // Setup refresh listener which triggers new data loading
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                // Your code to refresh the list here.
//                // Make sure you call swipeContainer.setRefreshing(false)
//                // once the network request has completed successfully.
//                fetchTimelineAsync(0);
//            }
//        });
//        // Configure the refreshing colors
//        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_red_light);
//
//    }
//
//    // for pull to refresh
//    public void fetchTimelineAsync(int page) {
//        client.getHomeTimeline(new JsonHttpResponseHandler() {
//            // success
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
//                Log.d("Debug", json.toString());
//                // deserialize json
//                // create models and add them to adapter
//                // load model data into listview
//
//                tweets.clear();
//                aTweets.notifyDataSetChanged();
//                // aTweets.clear();
//
//                ArrayList<Tweet> tweets = Tweet.fromJSONArray(json);
//
//                // https://dev.twitter.com/rest/public/timelines
//                // find lowest id from tweets, as max_id
//                Tweet t = tweets.get(tweets.size()-1);  // last element in ArrayList
//                lowestId = t.getUid();
//                Log.d("debug", "maxId: " + lowestId);
//
//                tweets.addAll(tweets);
//                aTweets.notifyDataSetChanged();
//                //aTweets.addAll(tweets);
//
//                // Now we call setRefreshing(false) to signal refresh has finished
//                swipeContainer.setRefreshing(false);
//
//            }
//
//            // failure
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                Log.d("Debug", errorResponse.toString());
//            }
//        });
//    }
//
//        // send api request to get timeline json
//    // fill listview by creating tweet objects from json
//    private void populateTimeline() {
//        // at 45:23 in video
//        client.getHomeTimeline(new JsonHttpResponseHandler() {
//            // success
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
//                Log.d("Debug", json.toString());
//                // deserialize json
//                // create models and add them to adapter
//                // load model data into listview
//
//                tweets.clear();
//                aTweets.notifyDataSetChanged();
//                // aTweets.clear();
//
//                ArrayList<Tweet> tweets = Tweet.fromJSONArray(json);
//
//                // https://dev.twitter.com/rest/public/timelines
//                // find lowest id from tweets, as max_id
//                Tweet t = tweets.get(tweets.size()-1);  // last element in ArrayList
//                lowestId = t.getUid();
//
//                aTweets.notifyItemChanged(0, tweets.size());
//
//                Log.d("debug", "aTweets count: " + aTweets.getItemCount());
//
////                aTweets.addAll(tweets);
//            }
//
//            // failure
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                Log.d("Debug", errorResponse.toString());
//            }
//        });
//    }
//
//    // used for endless scrolling
//    // Append the next page of data into the adapter
//    // This method probably sends out a network request and appends new data items to your adapter.
//    public void loadNextDataFromApi(int offset) {
//        // Send an API request to retrieve appropriate paginated data
//        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
//        //  --> Deserialize and construct new model objects from the API response
//        //  --> Append the new data objects to the existing set of items inside the array of items
//        //  --> Notify the adapter of the new items made with `notifyDataSetChanged()`
//
//        client.getHomeTimeline(new JsonHttpResponseHandler() {
//            // success
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
//                Log.d("Debug", json.toString());
//                // deserialize json
//                // create models and add them to adapter
//                // load model data into listview
//                ArrayList<Tweet> tweets = Tweet.fromJSONArray(json);
//
//                // find lowest id from tweets, as max_id
//                Tweet t = tweets.get(tweets.size()-1);  // last element in ArrayList
//                lowestId = t.getUid();
//                Log.d("debug", "maxId: " + lowestId);
//                tweets.addAll(tweets);
//                aTweets.notifyDataSetChanged();
////                aTweets.addAll(tweets);
//            }
//
//            // failure
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                Log.d("Debug", errorResponse.toString());
//            }
//            // this call has lowestId as second argument, subtract 1 as described in documentation
//        }, lowestId - 1);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_timeline, menu);
//        MenuItem composeItem = menu.findItem(R.id.action_compose);
//        composeItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                Intent i = new Intent(getApplicationContext(), ComposeActivity.class);
//                System.out.println("start filter activity");
//                startActivityForResult(i, REQUEST_CODE_COMPOSE);
//                Log.d("debug", "called startActivityForResult");
//                return true;
//            }
//        });
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_COMPOSE) {
//            // refresh timeline
//            populateTimeline();
//        }
//    }
//}
