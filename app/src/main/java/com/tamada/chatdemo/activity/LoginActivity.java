package com.tamada.chatdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tamada.chatdemo.R;
import com.tamada.chatdemo.helper.PreferManager;
import com.tamada.chatdemo.models.UserModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.input_name)
    EditText inputName;

    @BindView(R.id.idInputPassword)
    EditText etInputPasswrod;

    private Button btnLogin;
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

        btnLogin = (Button) findViewById(R.id.btn_login);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("users");

        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("value is", "" + dataSnapshot.getChildrenCount());
                userCount = dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @OnClick(R.id.btn_login)
    public void login() {
        String email = inputName.getText().toString().trim();
        String password = inputName.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), getString(R.string.msg_please_enter_details), Toast.LENGTH_SHORT).show();
        } else {
            userLogin(email, password);
        }
    }

    /**
     *
     * @param email
     * @param password
     */
    private void userLogin(String email, String password) {
        if (userCount >= 2) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_user_count_increased), Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(userId)) {
            //get child node id
            userId = mFirebaseDatabase.push().getKey();
        }

        UserModel user = new UserModel(userId, email, password, false);
        mFirebaseDatabase.child(userId).setValue(user);
        preferManager.storeUser(user);

        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
