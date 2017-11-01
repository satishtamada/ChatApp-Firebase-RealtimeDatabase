package com.tamada.chatdemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tamada.chatdemo.R;
import com.tamada.chatdemo.adapters.MessagesAdapter;
import com.tamada.chatdemo.helper.PreferManager;
import com.tamada.chatdemo.models.Message;
import com.tamada.chatdemo.models.User;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by inventbird on 31/10/17.
 */

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = ChatActivity.class.getSimpleName();

    @BindView(R.id.input_message)
    EditText etInputMessage;

    @BindView(R.id.btn_send)
    Button btnSendMessage;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private PreferManager preferManager;
    private FirebaseDatabase mFirebaseInstance;
    private String chatId;
    private String currentUserId,  currentUserName,currentUserEmail;
    private MessagesAdapter messagesAdapter;
    private ArrayList<Message> latLongModelArrayList;
    private DatabaseReference mFirebaseDatabase;
    private ProgressBar progressBar;
    private User currentUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("messages");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        etInputMessage = (EditText) findViewById(R.id.idInputMessage);
        btnSendMessage = (Button) findViewById(R.id.idBtnSend);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        preferManager = new PreferManager(getApplicationContext());
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        currentUserId = preferManager.getUser().getId();
        currentUserName = preferManager.getUser().getUserName();
        currentUserEmail=preferManager.getUser().getEmail();

        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
        // get reference to 'latlong' node
        latLongModelArrayList = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        progressBar.setVisibility(View.VISIBLE);
        mFirebaseDatabase = mFirebaseInstance.getReference("messages");

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strInputMessge = etInputMessage.getText().toString().trim();
                if (strInputMessge.length() != 0) {
                    sendMessage(strInputMessge);
                }
            }
        });
        Log.e(TAG, "current User: " + currentUser);

        mFirebaseInstance.getReference("users").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                Log.e(TAG, "current User: " + currentUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "onDataChange");
                latLongModelArrayList.clear();
                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    Message note = noteSnapshot.getValue(Message.class);
                    latLongModelArrayList.add(note);
                }
                messagesAdapter = new MessagesAdapter(latLongModelArrayList, getApplicationContext(), currentUserName);
                recyclerView.setAdapter(messagesAdapter);
                /**
                 * once data fetched visible recyclerview and hide progressbar
                 */
                recyclerView.setVisibility(View.VISIBLE);
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
        if (TextUtils.isEmpty(chatId)) {
            //get child node id
            chatId = mFirebaseDatabase.push().getKey();
        }

        String messageId = mFirebaseDatabase.push().getKey();
        Message message = new Message(currentUser.getUserName(), currentUser.getId(), strInputMessge);
        mFirebaseDatabase.child(messageId).setValue(message);
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
