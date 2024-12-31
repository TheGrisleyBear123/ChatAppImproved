package com.example.androidstudiofinalproject;

import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidstudiofinalproject.Message;
import com.example.androidstudiofinalproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private DatabaseReference databaseReference;
    private Context mContext;
    private List<Message> mMessageList;

    public MessageAdapter(Context mContext, List<Message> mMessageList) {
        this.mContext = mContext;
        this.mMessageList = mMessageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        String email;
        Message message = mMessageList.get(position);
        String userId = message.getSenderId();

        holder.show_message.setText(message.getMessage());
        //.id_usernameChatitem.setText();
        // Display sender email
        //ONE THAT DOESNT CRASH Down one
        holder.id_usernameChatitem.setText(message.getSenderId());
        //holder.id_usernameChatitem.setText(email);

        //holder.id_usernameChatitem.setText(message.getSenderEmail());
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView show_message;
        public TextView id_usernameChatitem;

        public MessageViewHolder(View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            id_usernameChatitem = itemView.findViewById(R.id.id_usernameChatitem);
        }
    }
}
