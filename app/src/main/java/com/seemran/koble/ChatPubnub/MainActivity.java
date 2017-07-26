package com.seemran.koble.ChatPubnub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;
import com.seemran.koble.Activity.LoginActivity;
import com.seemran.koble.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    //    <--mainactivity--->
    private Pubnub mPubNub;
    private Button mChannelView;
    private EditText mMessageET;
    private MenuItem mHereNow;
    private ListView mListView;
    private ChatAdapter mChatAdapter;
    private SharedPreferences mSharedPrefs;

    private String username;
    private String channel  = "MainChat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //<----mainactivity onCreateBundle--->
        mSharedPrefs = getSharedPreferences(Constants.CHAT_PREFS, MODE_PRIVATE);
        if (!mSharedPrefs.contains(Constants.CHAT_USERNAME)){
            Intent toLogin = new Intent(this, LoginActivity.class);
            startActivity(toLogin);
            return;
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            Log.d("Main-bundle",extras.toString() + " Has Chat: " + extras.getString(Constants.CHAT_ROOM));
            if (extras.containsKey(Constants.CHAT_ROOM)) this.channel = extras.getString(Constants.CHAT_ROOM);
        }

        this.username = mSharedPrefs.getString(Constants.CHAT_USERNAME,"Anonymous");
        this.mListView = getListView();
        this.mChatAdapter = new ChatAdapter(this, new ArrayList<ChatMessage>());
        this.mChatAdapter.userPresence(this.username, "join"); // Set user to online. Status changes handled in presence
        setupAutoScroll();
        this.mListView.setAdapter(mChatAdapter);
        setupListView();

        this.mMessageET = (EditText) findViewById(R.id.message_et);
        this.mChannelView = (Button) findViewById(R.id.channel_bar);
        this.mChannelView.setText(this.channel);

        initPubNub();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // TODO: Update to store messages in the array.
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.mHereNow = menu.findItem(R.id.action_here_now);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id){
            case R.id.action_here_now:
                hereNow(true);
                return true;
            case R.id.action_sign_out:
                signOut();
                return true;
            case R.id.action_gcm_register:
                gcmRegister();
                return true;
            case R.id.action_gcm_unregister:
                gcmUnregister();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * Might want to unsubscribe from PubNub here and create background service to listen while
     *   app is not in foreground.
     * PubNub will stop subscribing when screen is turned off for this demo, messages will be loaded
     *   when app is opened through a call to history.
     * The best practice would be creating a background service in onStop to handle messages.
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (this.mPubNub != null)
            this.mPubNub.unsubscribeAll();
    }

    /**
     * Instantiate PubNub object if it is null. Subscribe to channel and pull old messages via
     *   history.
     */

    @Override
    protected void onRestart() {
        super.onRestart();
        if (this.mPubNub==null){
            initPubNub();
        } else {
            subscribeWithPresence();
            history();
        }
    }

    /**
     * I remove the PubNub object in onDestroy since turning the screen off triggers onStop and
     *   I wanted PubNub to receive messages while the screen is off.
     *
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Instantiate PubNub object with username as UUID
     *   Then subscribe to the current channel with presence.
     *   Finally, populate the listview with past messages from history
     */
    private void initPubNub(){
        this.mPubNub = new Pubnub(Constants.PUBLISH_KEY, Constants.SUBSCRIBE_KEY);
        this.mPubNub.setUUID(this.username);
        subscribeWithPresence();
        history();
        gcmRegister();
    }

    /**
     * Use PubNub to send any sort of data
     * @param type The type of the data, used to differentiate groupMessage from directMessage
     * @param data The payload of the publish
     */
    public void publish(String type, JSONObject data){
        JSONObject json = new JSONObject();
        try {
            json.put("type", type);
            json.put("data", data);
        } catch (JSONException e) { e.printStackTrace(); }

        this.mPubNub.publish(this.channel, json, new BasicCallback());
    }
    /**
     * Update here now number, uses a call to the pubnub hereNow function.
     * @param displayUsers If true, display a modal of users in room.
     */
    public void hereNow(final boolean displayUsers) {
        this.mPubNub.hereNow(this.channel, new Callback() {
            @Override
            public void successCallback(String channel, Object response) {
                try {
                    JSONObject json = (JSONObject) response;
                    final int occ = json.getInt("occupancy");
                    final JSONArray hereNowJSON = json.getJSONArray("uuids");
                    Log.d("JSON_RESP", "Here Now: " + json.toString());
                    final Set<String> usersOnline = new HashSet<String>();
                    usersOnline.add(username);
                    for (int i = 0; i < hereNowJSON.length(); i++) {
                        usersOnline.add(hereNowJSON.getString(i));
                    }
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mHereNow != null)
                                mHereNow.setTitle(String.valueOf(occ));
                            mChatAdapter.setOnlineNow(usersOnline);
                            if (displayUsers)
                                alertHereNow(usersOnline);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    /**
     * Called at login time, sets meta-data of users' log-in times using the PubNub State API.
     *   Information is retrieved in getStateLogin
     */
    public void setStateLogin(){
        Callback callback = new Callback() {
            @Override
            public void successCallback(String channel, Object response) {
                Log.d("PUBNUB", "State: " + response.toString());
            }
        };
        try {
            JSONObject state = new JSONObject();
            state.put(Constants.STATE_LOGIN, System.currentTimeMillis());
            this.mPubNub.setState(this.channel, this.mPubNub.getUUID(), state, callback);
        }
        catch (JSONException e) { e.printStackTrace(); }
    }

    /**
     * Get state information. Information is deleted when user unsubscribes from channel
     *   so display a user not online message if there is no UUID data attached to the
     *   channel's state
     * @param user
     */
    public void getStateLogin(final String user){
        Callback callback = new Callback() {
            @Override
            public void successCallback(String channel, Object response) {
                if (!(response instanceof JSONObject)) return; // Ignore if not JSON
                try {
                    JSONObject state = (JSONObject) response;
                    final boolean online = state.has(Constants.STATE_LOGIN);
                    final long loginTime = online ? state.getLong(Constants.STATE_LOGIN) : 0;

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!online)
                                Toast.makeText(MainActivity.this, user + " is not online.", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(MainActivity.this, user + " logged in since " + ChatAdapter.formatTimeStamp(loginTime), Toast.LENGTH_SHORT).show();

                        }
                    });

                    Log.d("PUBNUB", "State: " + response.toString());
                } catch (JSONException e){ e.printStackTrace(); }
            }
        };
        this.mPubNub.getState(this.channel, user, callback);
    }
    /**
     * Subscribe to channel, when subscribe connection is established, in connectCallback, subscribe
     *   to presence, set login time with setStateLogin and update hereNow information.
     * When a message is received, in successCallback, get the ChatMessage information from the
     *   received JSONObject and finally put it into the listview's ChatAdapter.
     * Chat adapter calls notifyDatasetChanged() which updates UI, meaning must run on UI thread.
     */
    public void subscribeWithPresence(){
        Callback subscribeCallback = new Callback() {
            @Override
            public void successCallback(String channel, Object message) {
                if (message instanceof JSONObject){
                    try {
                        JSONObject jsonObj = (JSONObject) message;
                        JSONObject json = jsonObj.getJSONObject("data");
                        String name = json.getString(Constants.JSON_USER);
                        String msg  = json.getString(Constants.JSON_MSG);
                        long time   = json.getLong(Constants.JSON_TIME);
                        if (name.equals(mPubNub.getUUID())) return; // Ignore own messages
                        final ChatMessage chatMsg = new ChatMessage(name, msg, time);
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mChatAdapter.addMessage(chatMsg);
                            }
                        });
                    } catch (JSONException e){ e.printStackTrace(); }
                }
                Log.d("PUBNUB", "Channel: " + channel + " Msg: " + message.toString());
            }

            @Override
            public void connectCallback(String channel, Object message) {
                Log.d("Subscribe","Connected! " + message.toString());
                hereNow(false);
                setStateLogin();
            }
        };
        try {
            mPubNub.subscribe(this.channel, subscribeCallback);
            presenceSubscribe();
        } catch (PubnubException e){ e.printStackTrace(); }
    }
    /**
     * Subscribe to presence. When user join or leave are detected, update the hereNow number
     *   as well as add/remove current user from the chat adapter's userPresence array.
     *   This array is used to see what users are currently online and display a green dot next
     *   to users who are online.
     */
    public void presenceSubscribe()  {
        Callback callback = new Callback() {
            @Override
            public void successCallback(String channel, Object response) {
                Log.i("PN-pres","Pres: " + response.toString() + " class: " + response.getClass().toString());
                if (response instanceof JSONObject){
                    JSONObject json = (JSONObject) response;
                    Log.d("PN-main","Presence: " + json.toString());
                    try {
                        final int occ = json.getInt("occupancy");
                        final String user = json.getString("uuid");
                        final String action = json.getString("action");
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mChatAdapter.userPresence(user, action);
                                mHereNow.setTitle(String.valueOf(occ));
                            }
                        });
                    } catch (JSONException e){ e.printStackTrace(); }
                }
            }
            @Override
            public void errorCallback(String channel, PubnubError error) {
                Log.d("Presence", "Error: " + error.toString());
            }
        };
        try {
            this.mPubNub.presence(this.channel, callback);
        } catch (PubnubException e) { e.printStackTrace(); }
    }

    /**
     * Get last 100 messages sent on current channel from history.
     */
    public void history(){
        this.mPubNub.history(this.channel,100,false,new Callback() {
            @Override
            public void successCallback(String channel, final Object message) {
                try {
                    JSONArray json = (JSONArray) message;
                    Log.d("History", json.toString());
                    final JSONArray messages = json.getJSONArray(0);
                    final List<ChatMessage> chatMsgs = new ArrayList<ChatMessage>();
                    for (int i = 0; i < messages.length(); i++) {
                        try {
                            if (!messages.getJSONObject(i).has("data")) continue;
                            JSONObject jsonMsg = messages.getJSONObject(i).getJSONObject("data");
                            String name = jsonMsg.getString(Constants.JSON_USER);
                            String msg = jsonMsg.getString(Constants.JSON_MSG);
                            long time = jsonMsg.getLong(Constants.JSON_TIME);
                            ChatMessage chatMsg = new ChatMessage(name, msg, time);
                            chatMsgs.add(chatMsg);
                        } catch (JSONException e) { // Handle errors silently
                            e.printStackTrace();
                        }
                    }

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,"RUNNIN",Toast.LENGTH_SHORT).show();
                            mChatAdapter.setMessages(chatMsgs);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void errorCallback(String channel, PubnubError error) {
                Log.d("History", error.toString());
            }
        });
    }
}
