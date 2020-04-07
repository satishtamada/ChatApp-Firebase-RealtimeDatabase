package com.tamada.chatdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.tamada.chatdemo.R;
import com.tamada.chatdemo.databinding.ActivityWelcomeBinding;
import com.tamada.chatdemo.helper.AppController;
import com.tamada.chatdemo.helper.PreferManager;


/**
 * Created by LSN-ANDROID2 on 02-11-2018.
 */

public class WelcomeActivity extends AppCompatActivity {

    private PreferManager preferManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityWelcomeBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome);
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        preferManager=new PreferManager(AppController.getInstance().getApplicationContext());
        if (preferManager.getUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
            return;
        }

        binding.idBtnLogin.setOnClickListener(v -> startActivity(new Intent(WelcomeActivity.this,LoginActivity.class)));

        binding.idBtnSignup.setOnClickListener(v -> startActivity(new Intent(WelcomeActivity.this,SignupActivity.class)));
    }
}
