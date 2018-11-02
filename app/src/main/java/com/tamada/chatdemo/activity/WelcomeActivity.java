package com.tamada.chatdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tamada.chatdemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LSN-ANDROID2 on 02-11-2018.
 */

public class WelcomeActivity extends AppCompatActivity {

    @BindView(R.id.idBtnLogin)
    Button btnLogin;

    @BindView(R.id.idBtnSignup)
    Button btnSignup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
    }

    @OnClick(R.id.idBtnLogin)
    public void onLoginClicked() {
        startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
    }

    @OnClick(R.id.idBtnSignup)
    public void onSignupClicked() {
        startActivity(new Intent(WelcomeActivity.this,SignupActivity.class));

    }
}
