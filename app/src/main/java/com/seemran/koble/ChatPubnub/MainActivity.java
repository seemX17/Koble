package com.seemran.koble.ChatPubnub;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.pubnub.api.Callback;
import com.pubnub.api.PnGcmMessage;
import com.pubnub.api.PnMessage;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;
import com.seemran.koble.Activity.LoginActivity;
import com.seemran.koble.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends Fragment{
    private Pubnub mPubNub;
    private Button mChannelView;
    private EditText mMessageET;
    private MenuItem mHereNow;
    private ListView mListView;
    private ChatAdapter mChatAdapter;
    private SharedPreferences mSharedPrefs;

    private String username;
    private String channel  = "MainChat";
    Context ctx;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        ctx = getActivity();
        mSharedPrefs = ctx.getSharedPreferences(Constants.CHAT_PREFS, ctx.MODE_PRIVATE);
        if (!mSharedPrefs.contains(Constants.CHAT_USERNAME)){
            Intent toLogin = new Intent(ctx, LoginActivity.class);
            startActivity(toLogin);
        }

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null){
            Log.d("Main-bundle",extras.toString() + " Has Chat: " + extras.getString(Constants.CHAT_ROOM));
            if (extras.containsKey(Constants.CHAT_ROOM)) this.channel = extras.getString(Constants.CHAT_ROOM);
        }

        this.username = mSharedPrefs.getString(Constants.CHAT_USERNAME,"Anonymous");
        this.mListView = getListView();
        this.mChatAdapter = new ChatAdapter(ctx, new ArrayList<ChatMessage>());
        this.mChatAdapter.userPresence(this.username, "join"); // Set user to online. Status changes handled in presence
        setupAutoScroll();
        this.mListView.setAdapter(mChatAdapter);
//        setupListView();

        this.mMessageET = (EditText) view.findViewById(R.id.message_et);
        this.mChannelView = (Button) view.findViewById(R.id.channel_bar);
        this.mChannelView.setText(this.channel);

        initPubNub();
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
     public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.Planets, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
        // TODO: Update to store messages in the array.
    }
    @Override
    public void onStop() {
        super.onStop();
        if (this.mPubNub != null)
            this.mPubNub.unsubscribeAll();
    }

    /**
     * Instantiate PubNub object if it is null. Subscribe to channel and pull old messages via
     *   history.
     */

    protected void onRestart() {
        super.onResume();
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
    public void onDestroy() {
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
       // gcmRegister();
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

        this.mPubNub.publish(this.channel, json, new BasicCallBack());
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
                    getActivity().runOnUiThread(new Runnable() {
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

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!online)
                            {
                                Toast.makeText(getActivity(), "Please check your username and password!", Toast.LENGTH_SHORT).show();
                               Toast.makeText(getActivity(), user + " is not online.", Toast.LENGTH_SHORT).show();}
                            else
                                Toast.makeText(getActivity(), user + " logged in since " + ChatAdapter.formatTimeStamp(loginTime), Toast.LENGTH_SHORT).show();

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
                        getActivity().runOnUiThread(new Runnable() {
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
                        getActivity().runOnUiThread(new Runnable() {
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

                   getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"RUNNIN",Toast.LENGTH_SHORT).show();
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

    /**
     * Log out, remove username from SharedPreferences, unsubscribe from PubNub, and send user back
     *   to the LoginActivity
     */
    public void signOut(){
        this.mPubNub.unsubscribeAll();
        SharedPreferences.Editor edit = mSharedPrefs.edit();
        edit.remove(Constants.CHAT_USERNAME);
        edit.apply();
        Intent intent = new Intent(ctx, LoginActivity.class);
        intent.putExtra("oldUsername", this.username);
        startActivity(intent);
    }

    /**
     * Setup the listview to scroll to bottom anytime it receives a message.
     */
    private void setupAutoScroll(){
        this.mChatAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                mListView.setSelection(mChatAdapter.getCount() - 1);
                // mListView.smoothScrollToPosition(mChatAdapter.getCount()-1);
            }
        });
    }

//    /**
//     * On message click, display the last time the user logged in.
//     */
//    private void setupListView(){
//        this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ChatMessage chatMsg = mChatAdapter.getItem(position);
//                sendNotification(chatMsg.getUsername());
//            }
//        });
//    }

    /**
     * Publish message to current channel.
     * @param view The 'SEND' Button which is clicked to trigger a sendMessage call.
     */
    public void sendMessage(View view){
        String message = mMessageET.getText().toString();
        if (message.equals("")) return;
        mMessageET.setText("");
        ChatMessage chatMsg = new ChatMessage(username, message, System.currentTimeMillis());
        try {
            JSONObject json = new JSONObject();
            json.put(Constants.JSON_USER, chatMsg.getUsername());
            json.put(Constants.JSON_MSG,  chatMsg.getMessage());
            json.put(Constants.JSON_TIME, chatMsg.getTimeStamp());
            publish(Constants.JSON_GROUP, json);
        } catch (JSONException e){ e.printStackTrace(); }
        mChatAdapter.addMessage(chatMsg);
    }

    /**
     * Create an alert dialog with a list of users who are here now.
     * When a user's name is clicked, get their state information and display it with Toast.
     * @param userSet
     */
    private void alertHereNow(Set<String> userSet){
        List<String> users = new ArrayList<String>(userSet);
        LayoutInflater li = LayoutInflater.from(getActivity());
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Here Now");
        alertDialog.setNegativeButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final ArrayAdapter<String> hnAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,users);
        alertDialog.setAdapter(hnAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String user = hnAdapter.getItem(which);
                getStateLogin(user);
            }
        });
        alertDialog.show();
    }

    /**
     * Create an alert dialog with a text view to enter a new channel to join. If the channel is
     *   not empty, unsubscribe from the current channel and join the new one.
     *   Then, get messages from history and update the channelView which displays current channel.
     * @param view
     */
    public void changeChannel(View view){
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.channel_change, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
        userInput.setText(this.channel);                       // Set text to current ID
        userInput.setSelection(userInput.getText().length());  // Move cursor to end

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String newChannel = userInput.getText().toString();
                                if (newChannel.equals("")) return;

                                mPubNub.unsubscribe(channel);
                                mChatAdapter.clearMessages();
                                channel = newChannel;
                                mChannelView.setText(channel);
                                subscribeWithPresence();
                                history();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

//    /**
//     * GCM Functionality.
//     * In order to use GCM Push notifications you need an API key and a Sender ID.
//     * Get your key and ID at - https://developers.google.com/cloud-messaging/
//     */
//
//    private void gcmRegister() {
//        if (checkPlayServices()) {
//            gcm = GoogleCloudMessaging.getInstance(this);
//            try {
//                gcmRegId = getRegistrationId();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            if (gcmRegId.isEmpty()) {
//                registerInBackground();
//            } else {
//                Toast.makeText(this,"Registration ID already exists: " + gcmRegId,Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            Log.e("GCM-register", "No valid Google Play Services APK found.");
//        }
//    }
//
//    private void gcmUnregister() {
//        new UnregisterTask().execute();
//    }
//
//    private void removeRegistrationId() {
//        SharedPreferences prefs = getSharedPreferences(Constants.CHAT_PREFS, Context.MODE_PRIVATE);
//
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.remove(Constants.GCM_REG_ID);
//        editor.apply();
//    }
//
//    public void sendNotification(String toUser) {
//        PnGcmMessage gcmMessage = new PnGcmMessage();
//        JSONObject json = new JSONObject();
//        try {
//            json.put(Constants.GCM_POKE_FROM, this.username);
//            json.put(Constants.GCM_CHAT_ROOM, this.channel);
//            gcmMessage.setData(json);
//
//            PnMessage message = new PnMessage(
//                    this.mPubNub,
//                    toUser,
//                    new BasicCallback(),
//                    gcmMessage);
//            message.put("pn_debug",true); // Subscribe to yourchannel-pndebug on console for reports
//            message.publish();
//        }
//        catch (JSONException e) { e.printStackTrace(); }
//        catch (PubnubException e) { e.printStackTrace(); }
//    }
//
//    private boolean checkPlayServices() {
//        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
//        if (resultCode != ConnectionResult.SUCCESS) {
//            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                GooglePlayServicesUtil.getErrorDialog(resultCode, this, Constants.PLAY_SERVICES_RESOLUTION_REQUEST).show();
//            } else {
//                Log.e("GCM-check", "This device is not supported.");
//                finish();
//            }
//            return false;
//        }
//        return true;
//    }
//
//    private void registerInBackground() {
//        new RegisterTask().execute();
//    }
//
//    private void storeRegistrationId(String regId) {
//        SharedPreferences prefs = getSharedPreferences(Constants.CHAT_PREFS, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putString(Constants.GCM_REG_ID, regId);
//        editor.apply();
//    }
//
//
//    private String getRegistrationId() {
//        SharedPreferences prefs = getSharedPreferences(Constants.CHAT_PREFS, Context.MODE_PRIVATE);
//        return prefs.getString(Constants.GCM_REG_ID, "");
//    }
//
//    private void sendRegistrationId(String regId) {
//        this.mPubNub.enablePushNotificationsOnChannel(this.username, regId, new BasicCallback());
//    }
//
//    private class RegisterTask extends AsyncTask<Void, Void, String> {
//        @Override
//        protected String doInBackground(Void... params) {
//            String msg="";
//            try {
//                if (gcm == null) {
//                    gcm = GoogleCloudMessaging.getInstance(MainActivity.this);
//                }
//                gcmRegId = gcm.register(Constants.GCM_SENDER_ID);
//                msg = "Device registered, registration ID: " + gcmRegId;
//
//                sendRegistrationId(gcmRegId);
//
//                storeRegistrationId(gcmRegId);
//                Log.i("GCM-register", msg);
//            } catch (IOException e){
//                e.printStackTrace();
//            }
//            return msg;
//        }
//    }
//
//    private class UnregisterTask extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected Void doInBackground(Void... params) {
//            try {
//                if (gcm == null) {
//                    gcm = GoogleCloudMessaging.getInstance(MainActivity.this);
//                }
//
//                // Unregister from GCM
//                gcm.unregister();
//
//                // Remove Registration ID from memory
//                removeRegistrationId();
//
//                // Disable Push Notification
//                mPubNub.disablePushNotificationsOnChannel(username, gcmRegId);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//    }
}
