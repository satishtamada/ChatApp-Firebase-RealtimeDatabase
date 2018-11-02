package com.tamada.chatdemo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tamada.chatdemo.R;
import com.tamada.chatdemo.adapters.ContactsAdapter;
import com.tamada.chatdemo.adapters.MessagesAdapter;
import com.tamada.chatdemo.helper.PreferManager;
import com.tamada.chatdemo.models.ContactModel;
import com.tamada.chatdemo.models.MessagesModel;
import com.tamada.chatdemo.models.UserModel;
import com.tamada.chatdemo.receivers.ConnectivityReceiver;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity{
    @BindView(R.id.friendName)
    TextView lblFriendName;

    @BindView(R.id.friendEmail)
    TextView lblFriendEmail;

    @BindView(R.id.emptyList)
    TextView lblEmptyList;

    @BindView(R.id.idChatHead)
    CardView layoutChatHead;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private PreferManager preferManager;
    private String currentUserId,  currentUserEmail,currentUserName;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String strChatPersonName,strFriendEmail;
    private UserModel currentUserModel;

    private ContactsAdapter messagesAdapter;
    private ArrayList<ContactModel> latLongModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        preferManager = new PreferManager(getApplicationContext());
        currentUserId = preferManager.getUser().getId();
        currentUserEmail = preferManager.getUser().getEmail();
        currentUserName=preferManager.getUser().getUserName();
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Your Friends");
        }
        /**
         * checks is internet enable or not
         */
        if (!ConnectivityReceiver.isConnected()) {
            Toast.makeText(getApplicationContext(), getString(R.string.lbl_error_internet), Toast.LENGTH_SHORT).show();
        }
        Log.e("user is", currentUserId + "," + currentUserEmail);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
        progressBar.setVisibility(View.VISIBLE);
        /**
         * get current user from real time database
         */
        mFirebaseDatabase.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUserModel = dataSnapshot.getValue(UserModel.class);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });

        /**
         * get friend details
         */
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    UserModel note = noteSnapshot.getValue(UserModel.class);
                    Log.e("here", note.getId());
                    //if users is available or not
                    if (dataSnapshot.getChildrenCount() > 1) {
                        if (!currentUserId.equals(note.getId())) {
                            strChatPersonName = note.getUserName();
                            strFriendEmail = note.getEmail();
                            if (strChatPersonName != null) {
                                lblFriendName.setText(strChatPersonName);
                                lblFriendEmail.setText(strFriendEmail);
                                layoutChatHead.setVisibility(View.VISIBLE);
                                lblEmptyList.setVisibility(View.GONE);

                            }
                        }
                    }else{
                        /**
                         * display if no user avilable show empty list
                         */
                       lblEmptyList.setVisibility(View.VISIBLE);
                       layoutChatHead.setVisibility(View.GONE);
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });


        layoutChatHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ChatActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_logout, menu);//Menu Resource, Menu
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.idLogout:
                /**
                 * event logout clear mobile login session
                 * remove user from realtime database
                 * show toast message
                 */
                preferManager.clearSession();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("users").child(currentUserId).removeValue();
                Toast.makeText(getApplicationContext(),"Successfully logged out..",Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
