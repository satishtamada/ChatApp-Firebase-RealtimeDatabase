package com.tamada.chatdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import com.tamada.chatdemo.helper.AppController;
import com.tamada.chatdemo.helper.PreferManager;
import com.tamada.chatdemo.models.ContactModel;
import com.tamada.chatdemo.models.UserModel;
import com.tamada.chatdemo.receivers.ConnectivityReceiver;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.emptyList)
    TextView lblEmptyList;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    private PreferManager preferManager;
    private String currentUserId, currentUserEmail, currentUserName;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private UserModel currentUserModel;

    private ContactsAdapter contactsAdapter;
    private ArrayList<ContactModel> contactModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Friends");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ButterKnife.bind(this);
        preferManager = new PreferManager(getApplicationContext());
        currentUserId = preferManager.getUser().getId();
        currentUserEmail = preferManager.getUser().getEmail();
        currentUserName = preferManager.getUser().getUserName();
        contactModelArrayList = new ArrayList<>();
        contactsAdapter = new ContactsAdapter(contactModelArrayList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(contactsAdapter);

        /**
         * checks is internet enable or not
         */
        if (!ConnectivityReceiver.isConnected()) {
            Toast.makeText(getApplicationContext(), getString(R.string.lbl_error_internet), Toast.LENGTH_SHORT).show();
        }
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
        progressBar.setVisibility(View.VISIBLE);
        /**
         * get all user from the database
         */
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contactModelArrayList.clear();
                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    ContactModel note = noteSnapshot.getValue(ContactModel.class);
                    assert note != null;
                    if (!note.getId().equals(currentUserId))//remove current user from the list
                        contactModelArrayList.add(note);
                }
                if (contactModelArrayList.size() > 0) {
                    contactsAdapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                    lblEmptyList.setVisibility(View.GONE);
                } else {
                    lblEmptyList.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });

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

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(AppController.getInstance().getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ContactModel contactModel = contactModelArrayList.get(position);
                Intent intent = new Intent(AppController.getInstance().getApplicationContext(), ChatActivity.class);
                intent.putExtra("friendId", contactModel.getId());
                intent.putExtra("friendName", contactModel.getUserName());
                // intent.putExtra("bundle", args);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

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
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                Toast.makeText(getApplicationContext(), "Successfully logged out..", Toast.LENGTH_LONG).show();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
