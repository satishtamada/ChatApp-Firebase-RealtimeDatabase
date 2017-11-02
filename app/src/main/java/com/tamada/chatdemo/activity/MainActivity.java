package com.tamada.chatdemo.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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
import com.razorpay.Checkout;
import com.razorpay.ExternalWalletListener;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.tamada.chatdemo.R;
import com.tamada.chatdemo.helper.PreferManager;
import com.tamada.chatdemo.models.User;
import com.tamada.chatdemo.receivers.ConnectivityReceiver;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements PaymentResultListener, ExternalWalletListener {
    @BindView(R.id.friendName)
    TextView lblFriendName;

    @BindView(R.id.friendEmail)
    TextView lblFriendEmail;

    @BindView(R.id.emptyList)
    TextView lblEmptyList;

    @BindView(R.id.idChatHead)
    CardView layoutChatHead;

    @BindView(R.id.fab)
    FloatingActionButton fab;


    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private PreferManager preferManager;
    private String currentUserId,  currentUserEmail,currentUserName;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String strChatPersonName,strFriendEmail;
    private User currentUser;


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
                currentUser = dataSnapshot.getValue(User.class);
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
                    User note = noteSnapshot.getValue(User.class);
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
                                fab.setVisibility(View.VISIBLE);
                                lblEmptyList.setVisibility(View.GONE);

                            }
                        }
                    }else{
                        /**
                         * display if no user avilable show empty list
                         */
                       lblEmptyList.setVisibility(View.VISIBLE);
                       layoutChatHead.setVisibility(View.GONE);
                       fab.setVisibility(View.GONE);
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
                checkPaymentStatus();

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPaymentStatus();
            }
        });
    }

    /**
     * method checks user payment details is paid or not
     * if payment is not paid then show dialog for make a payment
     */
    private void checkPaymentStatus() {
        if (currentUser.isPaid()) {
            startActivity(new Intent(getApplicationContext(), ChatActivity.class));
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle(getString(R.string.lbl_alert_title));
            alert.setMessage(getString(R.string.lbl_alert_desc));
            alert.setPositiveButton(getString(R.string.lbl_yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    makePayment(currentUserName,"1234567890", currentUserEmail, "100");
                    dialog.dismiss();
                }
            });

            alert.setNegativeButton(getString(R.string.lbl_no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }
    }

    /**
     * method lunch the payment sdk
     * @param name user name
     * @param mobile user mobile( set default mobile number)
     * @param email user email
     * @param amount input amount
     */
    public void makePayment(String name,String mobile, String email, String amount) {
        /**
         * You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;
        final Checkout co = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put("name", name);
            options.put("description", "Chat");
            options.put("currency", "INR");
            options.put("amount", amount);
            JSONObject preFill = new JSONObject();
            preFill.put("email", email);
            preFill.put("contact", mobile);
            options.put("prefill", preFill);
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment ", Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    @Override
    public void onExternalWalletSelected(String s, PaymentData paymentData) {

    }

    /**
     * if payment success then update user payment status ture at database
     * once user datails updated then lunch chat activity
     * @param razorpayPaymentID
     */
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child("users").child(currentUserId).child("paid").setValue(true);
            startActivity(new Intent(getApplicationContext(), ChatActivity.class));
        } catch (Exception e) {
            // TODO handle this error
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
    // TODO handle this error
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
