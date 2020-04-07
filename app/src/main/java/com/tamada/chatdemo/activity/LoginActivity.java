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
import com.tamada.chatdemo.databinding.ActivityLoginBinding;
import com.tamada.chatdemo.helper.AppController;
import com.tamada.chatdemo.helper.PreferManager;
import com.tamada.chatdemo.models.UserModel;
import com.tamada.chatdemo.receivers.ConnectivityReceiver;


/**
 * class login user into server
 */
public class LoginActivity extends AppCompatActivity {

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String userId;
    private long userCount;
    private PreferManager preferManager;

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferManager = new PreferManager(getApplicationContext());
        binding= DataBindingUtil.setContentView(this,R.layout.activity_login);
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
       binding.idBtnLogin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String email = binding.inputEmail.getText().toString().trim();
               String password = binding.idInputPassword.getText().toString();
               if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                   Toast.makeText(getApplicationContext(), getString(R.string.msg_please_enter_details), Toast.LENGTH_SHORT).show();
               } else if (!isValidEmail(email)) {
                   Toast.makeText(getApplicationContext(), getString(R.string.lbl_email_error), Toast.LENGTH_SHORT).show();
               } else {
                   binding.progressBar.setVisibility(View.VISIBLE);
                   userLogin(email, password);
               }
           }
       });
    }


    /**
     * method checks number of users in realtime database if count is less than two it allows to
     * create a user in to database otherwise shows error message
     *
     * @param email    input email
     * @param password input password
     */
   private void userLogin(String email, String password) {
        String apiKey = email.replace(".", "-");
        mFirebaseDatabase.child(apiKey + password).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    if (dataSnapshot.getChildrenCount() > 0) {
                        UserModel userModel = dataSnapshot.getValue(UserModel.class);
                        if (userModel != null) {
                            preferManager.storeUser(userModel);
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(AppController.getInstance().getApplicationContext(), "Unable to fetch the user info", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(AppController.getInstance().getApplicationContext(), "User not exits", Toast.LENGTH_SHORT).show();
                    }
                    binding.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });


    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
