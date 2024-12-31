package com.example.androidstudiofinalproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {
    private TextView name;
    private RecyclerView roomsRecyclerView;
    private RoomAdapter roomAdapter;
    private List<Room> roomList;
    private Button logout;
    private FirebaseAuth mAuth;
    private Button CreateRoomButton;
    private EditText CreateRoomField;
    private DatabaseReference db;
    String e;
    String p;
    String roomId;
    FirebaseUser currentuser;
    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mAuth = FirebaseAuth.getInstance();
        CreateRoomButton = findViewById(R.id.id_createButton);
        CreateRoomField = findViewById(R.id.id_createRoomField);

        name = findViewById(R.id.id_nameOnchatText);
        String emailfrommain = getIntent().getStringExtra("emailfrommain");
        //Log.d("email", emailfrommain);
        name.setText(emailfrommain);
        db = FirebaseDatabase.getInstance().getReference("Rooms");
        currentuser = mAuth.getCurrentUser();
        logout = findViewById(R.id.id_logout);

        roomsRecyclerView = findViewById(R.id.id_textViewChatsRecyclerView);
        roomsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        roomList = new ArrayList<Room>();
        roomAdapter = new RoomAdapter(roomList);
        roomsRecyclerView.setAdapter(roomAdapter);





        CreateRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newRoomName = CreateRoomField.getText().toString();
                if(!newRoomName.isEmpty()) {
                    createRoom(newRoomName);
                    sendTo(newRoomName);
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogOut();
            }
        });

        loadRooms();


    }

    private void sendTo(String roomName){
        Intent intent = new Intent(this, RoomActivity.class);
        e = getIntent().getStringExtra("email");
        p = getIntent().getStringExtra("passowrd");
        intent.putExtra("email", e);
        intent.putExtra("roomname", roomName);

        startActivity(intent);
        finish();
    }

    /*private void openRoom(Room room) {
        Intent intent = new Intent(this, RoomActivity.class);
        intent.putExtra("roomId", room.getRoomId());
        intent.putExtra("roomName", room.getRoomName());
        startActivity(intent);
        finish();
    }*/

    public void LogOut(){
        mAuth.signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private boolean checkForDuplicates() {
        String newRoomName = CreateRoomField.getText().toString().trim();
        for (Room room : roomList) {
            if (room.getRoomName().equals(newRoomName)) {
                return true;  // Duplicate found
            }
        }

        return false;  // No duplicates found
    }
    public interface RoomCreationCallback {
        void onDuplicateFound(boolean isDuplicate);
    }


    private void createRoom(String roomName){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentuser = mAuth.getCurrentUser();
        if(checkForDuplicates()) {
            Toast.makeText(this, "Name already in list joining selected room", Toast.LENGTH_SHORT).show();
        } else if(!checkForDuplicates()){
            if(currentuser != null) {
            roomId = UUID.randomUUID().toString();
            Room room = new Room(roomId, roomName, currentuser.getUid());
            databaseReference.child("Rooms").child(roomId).setValue(room)
                    .addOnSuccessListener(aVoid -> sendTo(roomName))
                    .addOnFailureListener(e -> Log.e("roomSend", "Failed to send Room", e));
             }
        }

    }


    private void loadRooms() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Rooms");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                roomList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Room room = dataSnapshot.getValue(Room.class);
                    roomList.add(room);
                }
                roomAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ChatActivity", "Failed to load rooms", error.toException());
            }
        });
    }


}
