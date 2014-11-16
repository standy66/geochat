package me.standy.geochat;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;


public class ChatActivity extends Activity {

    private Button sendButton;
    private EditText messageBox;
    private ListView chatHistory;
    private ChatArrayAdapter chatArrayAdapter;

    private boolean isMine = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        sendButton = (Button) findViewById(R.id.sendButton);
        messageBox = (EditText) findViewById(R.id.messageBox);
        chatHistory = (ListView) findViewById(R.id.chatHistory);

        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.single_message);
        chatHistory.setAdapter(chatArrayAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!messageBox.getText().toString().isEmpty()) {
                    chatArrayAdapter.add(new ChatMessage("Andey", messageBox.getText().toString(), isMine));
                    messageBox.setText("");
                    isMine = !isMine;
                    chatHistory.setSelection(chatArrayAdapter.getCount() - 1);
                    messageBox.requestFocus();
                }
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
