package me.standy.geochat;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.pubnub.api.PubnubException;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import java.util.List;


public class ChatActivity extends Activity implements ChatMessageService.OnChatMessageArriveCallback {

    private Button sendButton;
    private EditText messageBox;
    private ListView chatHistory;
    private ChatArrayAdapter chatArrayAdapter;
    private String userName;
    private String vkId;
    private ChatMessageService chatMessageService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        sendButton = (Button) findViewById(R.id.sendButton);
        messageBox = (EditText) findViewById(R.id.messageBox);
        chatHistory = (ListView) findViewById(R.id.chatHistory);
        VKApi.users().get().executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                VKApiUserFull currentUser = ((VKList<VKApiUserFull>) response.parsedModel).get(0);
                userName = currentUser.first_name;
                vkId = "id" + String.valueOf(currentUser.id);
            }

            @Override
            public void onError(VKError error) {
                userName = "unknown";
            }
        });
        try {
            chatMessageService = new ChatMessageService(1000, this);
        } catch (PubnubException e) {
            throw new RuntimeException(e);
        }

        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.single_message);
        chatHistory.setAdapter(chatArrayAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!messageBox.getText().toString().isEmpty()) {
                    ChatMessage message = new ChatMessage(userName, vkId, messageBox.getText().toString(), true);
                    messageBox.setText("");
                    chatMessageService.sendMessage(message);
                    messageBox.requestFocus();
                }
            }
        });
    }



    @Override
    public void onChatMessageArrive(final ChatMessage message) {
        message.isMine = (message.vkId.equals(vkId));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chatArrayAdapter.add(message);
                chatHistory.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
