package com.example.mike.pedidos;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatActivity extends AppCompatActivity {


    public RecyclerView myRecylerView ;
    public List<Message> MessageList ;
    public ChatBoxAdapter chatBoxAdapter;
    public  EditText messagetxt ;
    public  Button send ;
    //declare socket object
    private Socket socket;

    public String Nickname ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        messagetxt = (EditText) findViewById(R.id.textMessage);
        send = (Button) findViewById(R.id.btnSend);
        // get the nickame of the user
        Nickname = "Nick";//(String)getIntent().getExtras().getString(MainActivity.NICKNAME);

        //setting up recyler
        MessageList = new ArrayList<>();
        myRecylerView = (RecyclerView) findViewById(R.id.messagelist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        myRecylerView.setLayoutManager(mLayoutManager);
        //myRecylerView.setItemAnimator(new DefaultItemAnimator());





        // message send action
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //retrieve the nickname and the message content and fire the event messagedetection
                if (!messagetxt.getText().toString().isEmpty()) {
                    Toast.makeText(ChatActivity.this, "clicked", Toast.LENGTH_SHORT).show();
                    socket.emit("messagedetection", Nickname, messagetxt.getText().toString());

                    messagetxt.setText("");

                   // Toast.makeText(ChatActivity.this, MessageList.get(0).getMessage().toString(), Toast.LENGTH_SHORT).show();


                }


            }
        });
        //connect you socket client to the server
        try {
            socket = IO.socket("http://192.168.100.199:3000");
            socket.connect();
            socket.emit("join", Nickname);
        } catch (URISyntaxException e) {
            e.printStackTrace();

        }


        //implementing socket listeners
        socket.on("userjoinedthechat", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = (String) args[0];

                        Toast.makeText(ChatActivity.this, data, Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        socket.on("userdisconnect", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = (String) args[0];

                        Toast.makeText(ChatActivity.this, data, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        socket.on("message", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            Toast.makeText(ChatActivity.this, "ok msage", Toast.LENGTH_SHORT).show();
                            //extract data from fired event

                            String nickname = data.getString("senderNickname");
                            String message = data.getString("message");

                            // make instance of message

                            Message m = new Message(nickname, message);


                            //add the message to the messageList

                            MessageList.add(m);

                            // add the new updated list to the dapter
                            chatBoxAdapter = new ChatBoxAdapter(MessageList);

                            // notify the adapter to update the recycler view

                            chatBoxAdapter.notifyDataSetChanged();

                            //set the adapter for the recycler view

                            myRecylerView.setAdapter(chatBoxAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                });
            }
        });
    }



    @Override
    protected void onDestroy(){
        super.onDestroy();

        socket.disconnect();
    }
}


