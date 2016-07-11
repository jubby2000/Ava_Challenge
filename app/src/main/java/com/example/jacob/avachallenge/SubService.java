package com.example.jacob.avachallenge;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SubService extends Service {
    public static final String LOG_TAG = SubService.class.getSimpleName();
    public SubService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.


//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        final Pubnub pubnub = new Pubnub(getString(R.string.publish_key), getString(R.string.subscribe_key));

        try {
            pubnub.subscribe(getString(R.string.user_id), new Callback() {
                        @Override
                        public void connectCallback(String channel, Object message) {
                            pubnub.publish(getString(R.string.user_id), "Hello from the PubNub Java SDK", new Callback() {});
                        }

                        @Override
                        public void disconnectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : DISCONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }

                        public void reconnectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : RECONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }

                        @Override
                        public void successCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : " + channel + " : "
                                    + message.getClass() + " : " + message.toString());

                            try {
                                JSONObject jsonObject = new JSONObject(message.toString());
                                String requestCommand = jsonObject.optString("requestCommand");
                                String speakerId = jsonObject.optString("speakerId");
                                String blocId = jsonObject.optString("blocId");
//                                int blocId = Integer.parseInt(jsonObject.optString("blocId"));
                                String transcript = jsonObject.optString("transcript");

//                                int blocId = Integer.parseInt(strBlocId.replaceAll("\\s+", ""));

//                                Log.v(LOG_TAG, "Incoming blocId is: " + blocId
//                                        + ". And incoming requestCommand is: " + requestCommand
//                                        + ". And incoming speakerId is: " + speakerId
//                                        + ". And incoming transcript is: " + transcript);



                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra("transcript", transcript);
                                broadcastIntent.putExtra("blocId", blocId);
                                broadcastIntent.putExtra("speakerId", speakerId);
                                broadcastIntent.putExtra("requestCommand", requestCommand);
//                            broadcastIntent.setAction(getResources().getString(R.string.NEW_BLOC));
                                broadcastIntent.setAction(getResources().getString(R.string.NEW_TRANSCRIPT));
                                sendBroadcast(broadcastIntent);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

//                            System.out.println(transcript);


                        }

                        @Override
                        public void errorCallback(String channel, PubnubError error) {
                            System.out.println("SUBSCRIBE : ERROR on channel " + channel
                                    + " : " + error.toString());
                        }
                    }
            );
        } catch (PubnubException e) {
            System.out.println(e.toString());
        }
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}
