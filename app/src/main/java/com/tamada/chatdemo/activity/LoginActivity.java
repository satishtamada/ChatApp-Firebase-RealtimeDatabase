package com.tamada.chatdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tamada.chatdemo.R;
import com.tamada.chatdemo.helper.PreferManager;
import com.tamada.chatdemo.models.UserModel;
import com.tamada.chatdemo.receivers.ConnectivityReceiver;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * class login user into server
 */
public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.input_email)
    EditText inputEmail;

    @BindView(R.id.idInputPassword)
    EditText etInputPasswrod;

    @BindView(R.id.idBtnLogin)
    Button btnLogin;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String userId;
    private long userCount;
    private PreferManager preferManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferManager = new PreferManager(getApplicationContext());
        if (preferManager.getUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (!ConnectivityReceiver.isConnected()) {
            Toast.makeText(getApplicationContext(), getString(R.string.lbl_error_internet), Toast.LENGTH_SHORT).show();
        }else{
            progressBar.setVisibility(View.VISIBLE);
        }
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
        /**
         * checks number of users in db
         */
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("value is", "" + dataSnapshot.getChildrenCount());
                userCount = dataSnapshot.getChildrenCount();
                Log.e("user count", "here" + userCount);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    /**
     * method login perform input validation
     */
    @OnClick(R.id.idBtnLogin)
    public void login() {
        String email = inputEmail.getText().toString().trim();
        String password = etInputPasswrod.getText().toString();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), getString(R.string.msg_please_enter_details), Toast.LENGTH_SHORT).show();
        } else if(!isValidEmail(email)){
            Toast.makeText(getApplicationContext(),getString(R.string.lbl_email_error),Toast.LENGTH_SHORT).show();
        }else {
            progressBar.setVisibility(View.VISIBLE);
            userLogin( email, password);
        }
    }

    /**
     * method checks number of users in realtime database if count is less than two it allows to
     * create a user in to database otherwise shows error message
     *
     * @param email    input email
     * @param password input password
     */
    private void userLogin( String email, String password) {
        if (userCount >= 2) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), getString(R.string.error_user_count_increased), Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(userId)) {
            //get child node id
            userId = mFirebaseDatabase.push().getKey();
        }
        //by default userModel payment is false
        UserModel userModel = new UserModel(userId, "", email, password, false);
        mFirebaseDatabase.child(userId).setValue(userModel);
        preferManager.storeUser(userModel);
        progressBar.setVisibility(View.GONE);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
