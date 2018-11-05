package com.tamada.chatdemo.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tamada.chatdemo.R;
import com.tamada.chatdemo.adapters.MessagesAdapter;
import com.tamada.chatdemo.helper.PreferManager;
import com.tamada.chatdemo.models.ConnectionModel;
import com.tamada.chatdemo.models.MessagesModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by satish.
 */

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = ChatActivity.class.getSimpleName();

    @BindView(R.id.input_message)
    EditText etInputMessage;

    @BindView(R.id.btn_send)
    ImageView btnSendMessage;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private PreferManager preferManager;
    private FirebaseDatabase firebaseDatabase;
    private String chatId;
    private String userId, userName, userEmail;
    private MessagesAdapter messagesAdapter;
    private ArrayList<MessagesModel> messagesModelArrayList;
    private DatabaseReference databaseReference;
    private String friendName;
    private String friendId;
    private String connectionId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        friendName = intent.getStringExtra("friendName");
        friendId = intent.getStringExtra("friendId");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(friendName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        preferManager = new PreferManager(getApplicationContext());
        userId = preferManager.getUser().getId();
        userName = preferManager.getUser().getUserName();
        userEmail = preferManager.getUser().getEmail();
        messagesModelArrayList = new ArrayList<>();
        messagesAdapter = new MessagesAdapter(messagesModelArrayList, getApplicationContext(), userName);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(messagesAdapter);
        // progressBar.setVisibility(View.VISIBLE);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("messages");


        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                String strInputMessge = etInputMessage.getText().toString().trim();
                if (strInputMessge.length() != 0) {
                    String chatId = databaseReference.push().getKey();

                    databaseReference.child(connectionId).child(chatId).setValue(new MessagesModel(userName, userId, strInputMessge));
                    etInputMessage.setText("");
                }

            }
        });

        databaseReference.child(userId + friendId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messagesModelArrayList.clear();
                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    MessagesModel note = noteSnapshot.getValue(MessagesModel.class);
                    assert note != null;
                    messagesModelArrayList.add(note);
                }
                if (messagesModelArrayList.size() > 0) {
                    messagesAdapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }


    private void sendMessage(String strInputMessge) {
     /*   if (TextUtils.isEmpty(chatId)) {
            //get child node id
            chatId = databaseReference.push().getKey();
        }*/
        String messageId = databaseReference.push().getKey();
        MessagesModel messagesModel = new MessagesModel(friendName, friendId, strInputMessge);
        ConnectionModel connectionModel = new ConnectionModel(userId + friendId, messagesModel);
        databaseReference.setValue(connectionModel);
        etInputMessage.setText("");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
