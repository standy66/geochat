package me.standy.geochat;

import android.util.Log;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by andrew on 16.11.14.
 */
public class ChatMessageService {
    private Pubnub pubnub;
    private static final String PUBNUB_PUBLISH_KEY = "pub-c-df56c277-d469-4744-860f-8b5fe623c54b";
    private static final String PUBNUB_SUBSCRIBE_KEY = "sub-c-ead58da0-6c28-11e4-b4e4-02ee2ddab7fe";
    private static final String LOG_TAG = "ChatMessageService";
    private static final String CHANNEL_NAME = "geochat";
    private long locationThreshold;
    private OnChatMessageArriveCallback callback;

    public ChatMessageService(long locationThreshold, OnChatMessageArriveCallback callback) throws PubnubException {
        this.locationThreshold = locationThreshold;
        pubnub = new Pubnub(PUBNUB_PUBLISH_KEY, PUBNUB_SUBSCRIBE_KEY);
        pubnub.subscribe(CHANNEL_NAME, new SubscribeCallback());
        this.callback = callback;

    }

    public void sendMessage(ChatMessage message) {
        pubnub.publish(CHANNEL_NAME, message.toJSON(), new PublishCallback());
    }

    private class SubscribeCallback extends Callback {
        @Override
        public void successCallback(String channel, Object message) {
            JSONObject jsonMessage = (JSONObject) message;
            Log.i(LOG_TAG, jsonMessage.toString());
            if (jsonMessage != null) {
                callback.onChatMessageArrive(ChatMessage.fromJSON(jsonMessage));

            } else {
                Log.w(LOG_TAG, "Got strange messageBody" + message);
            }
        }

        @Override
        public void errorCallback(String s, PubnubError pubnubError) {
            Log.e(LOG_TAG, pubnubError.toString());
        }
    }

    private class PublishCallback extends Callback {
        @Override
        public void successCallback(String s, Object o) {
            Log.d(LOG_TAG, o.toString());
        }

        @Override
        public void errorCallback(String s, PubnubError pubnubError) {
            Log.e(LOG_TAG, pubnubError.toString());
        }
    }


    public interface OnChatMessageArriveCallback {
        public void onChatMessageArrive(ChatMessage message);
    }
}
