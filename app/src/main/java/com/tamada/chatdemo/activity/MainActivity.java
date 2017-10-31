package com.tamada.chatdemo.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
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
import com.tamada.chatdemo.models.UserModel;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements PaymentResultListener, ExternalWalletListener {
    private TextView etPersonTwoName, messageCount;
    private RelativeLayout layoutChatHead;
    private PreferManager preferManager;
    private String userId, email;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    //private boolean isPaymentSuccess;
    private String strChatPersonName;
    private FloatingActionButton floatingActionButton;
    private UserModel currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etPersonTwoName = (TextView) findViewById(R.id.idName);
        messageCount = (TextView) findViewById(R.id.idMessageCount);
        layoutChatHead = (RelativeLayout) findViewById(R.id.idChatHead);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        preferManager = new PreferManager(getApplicationContext());
        userId = preferManager.getUser().getId();
        email = preferManager.getUser().getUserName();
        Log.e("user is", userId + "," + email);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("users");

        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(UserModel.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    UserModel note = noteSnapshot.getValue(UserModel.class);
                    Log.e("here", note.getId());
                    if (dataSnapshot.getChildrenCount() > 1) {
                        if (!userId.equals(note.getId())) {
                            strChatPersonName = note.getUserName();
                            if (strChatPersonName != null) {
                                etPersonTwoName.setText(strChatPersonName);
                            } else {
                                etPersonTwoName.setText("No friends found..!");
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        layoutChatHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser.isPaid()) {
                    startActivity(new Intent(getApplicationContext(), ChatActivity.class));
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Make Payment");
                    alert.setMessage("You need to make payment for read or write message");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            makePayment("1234567890", email, "100");
                            dialog.dismiss();
                        }
                    });

                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.show();
                }
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser.isPaid()) {
                    startActivity(new Intent(getApplicationContext(), ChatActivity.class));
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Make Payment");
                    alert.setMessage("You need to make payment for read or write message");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            makePayment("1234567890", email, "100");
                            dialog.dismiss();
                        }
                    });

                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.show();
                }
            }
        });
    }

    public void makePayment(String mobile, String email, String amount) {
        /**
         * You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;
        final Checkout co = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Chat Demo");
            options.put("description", "Chat");
            /*JSONArray wallets = new JSONArray();
            wallets.put("paytm");
            JSONObject externals = new JSONObject();
            externals.put("wallets", wallets);
            options.put("external", externals);*/
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

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child("users").child(userId).child("paid").setValue(true);
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
                preferManager.clearSession();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("user").child(userId).removeValue();
                Toast.makeText(getApplicationContext(),"Successfully logged out..",Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
