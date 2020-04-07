package com.tamada.chatdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tamada.chatdemo.R;
import com.tamada.chatdemo.databinding.ActivitySignupBinding;
import com.tamada.chatdemo.helper.PreferManager;
import com.tamada.chatdemo.models.UserModel;
import com.tamada.chatdemo.receivers.ConnectivityReceiver;


/**
 * Created by LSN-ANDROID2 on 02-11-2018.
 */

public class SignupActivity extends AppCompatActivity {

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String userId;
    private long userCount;
    private PreferManager preferManager;
    private ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferManager = new PreferManager(getApplicationContext());
        binding= DataBindingUtil.setContentView(this,R.layout.activity_signup);
        if (preferManager.getUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
            return;
        }

        if (!ConnectivityReceiver.isConnected()) {
            Toast.makeText(getApplicationContext(), getString(R.string.lbl_error_internet), Toast.LENGTH_SHORT).show();
        } else {
            binding.progressBar.setVisibility(View.VISIBLE);
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
                binding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });

        binding.idBtnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = binding.inputPhone.getText().toString().trim();
                String name = binding.inputName.getText().toString().trim();
                String password = binding.inputName.getText().toString();
                if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(name) || TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.msg_please_enter_details), Toast.LENGTH_SHORT).show();
                } else if (!validCellPhone(phone)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.lbl_phone_error), Toast.LENGTH_SHORT).show();
                } else {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    userLogin(name, phone, password);
                }
            }
        });
    }
    public boolean validCellPhone(String number)
    {
        return android.util.Patterns.PHONE.matcher(number).matches();
    }


    /**
     * method checks number of users in realtime database if count is less than two it allows to
     * create a user in to database otherwise shows error message
     *
     * @param name     input name
     * @param phone    input phone
     * @param password input password
     */
    private void userLogin(String name, String phone, String password) {
        if (TextUtils.isEmpty(userId)) {
            //get child node id
            userId = mFirebaseDatabase.push().getKey();
        }
        //by default userModel payment is false
        UserModel userModel = new UserModel(userId, name, phone, password);
        mFirebaseDatabase.child(phone).setValue(userModel);
        preferManager.storeUser(userModel);
        binding.progressBar.setVisibility(View.GONE);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
