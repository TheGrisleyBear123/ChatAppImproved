package com.example.androidstudiofinalproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidstudiofinalproject.Message;
import com.example.androidstudiofinalproject.MessageAdapter;
import com.example.androidstudiofinalproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RoomActivity extends AppCompatActivity {


    private Button backButton;
    private DatabaseReference db;
    private FirebaseAuth mAuth;
    private Button sendButton;
    private EditText textField;
    private RecyclerView recyclerView;
    private List<Message> messageList;
    private MessageAdapter messageAdapter;
    private String currentUserId;
   // private String roomId;
    private TextView textViewName;


    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        backButton = findViewById(R.id.id_backButton);
        //roomId = getIntent().getStringExtra("roomId");
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getUid();
        textViewName = findViewById(R.id.id_nameView);
        //ROOMNAME IS NOT THE PERSON YOU SEND IT TO
        String i = getIntent().getStringExtra("roomname");
        textViewName.setText(i);

        sendButton = findViewById(R.id.id_sendButton);
        textField = findViewById(R.id.id_textMessage);
        recyclerView = findViewById(R.id.id_messagesRecycler);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        /*roomList = new ArrayList<>();
        roomAdapter = new RoomAdapter(this, roomList);
        roomListTextview.setAdapter(roomAdapter);
        roomListTextview.setLayoutManager(new LinearLayoutManager(this));*/


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageText = textField.getText().toString().trim();
                if (!TextUtils.isEmpty(messageText)) {
                    sendMessage(currentUserId, i, messageText);
                    textField.setText("");  // Clear the input field
                } else {
                    Toast.makeText(RoomActivity.this, "Please enter a message", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RoomActivity.this, ChatActivity.class);
                //intent.putExtra("messagelist", (CharSequence) messageList);
                startActivity(intent);

            }
        });

        loadMessages(currentUserId, i, textField.getText().toString());
    }

    //OLD METHOD
    /*private void sendMessages(String sender, String receiver, String message) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Message message1 = new Message(sender, receiver, message, System.currentTimeMillis());
        databaseReference.child("Chats").push().setValue(message1)
                .addOnSuccessListener(aVoid -> Log.d("sendMessages", "Message sent successfully"))
                .addOnFailureListener(e -> Log.e("sendMessages", "Failed to send message", e));
    }*/

    //puts the messages on the database
    private void sendMessage(String sender, String roomName, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats").child(roomName).push();
        Message message1 = new Message(sender, roomName, message, System.currentTimeMillis());
        reference.setValue(message1).addOnSuccessListener(aVoid -> {
            Log.d("sendMessage", "Message sent successfully");
        }).addOnFailureListener(e -> {
            Log.e("sendMessage", "Failed to send messagae", e);
        });
    }

    //loads the messages on the phone
    private void loadMessages(String sender, String roomName, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats").child(roomName);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Message message = dataSnapshot.getValue(Message.class);
                    messageList.add(message);
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("fetchMessages", "Failed to fetch messages", error.toException());
            }
        });
    }

}
