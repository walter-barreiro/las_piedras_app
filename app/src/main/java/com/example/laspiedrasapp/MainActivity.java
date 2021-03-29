package com.example.laspiedrasapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button mButtonSignout;
    private TextView mTextViewEmail;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mTextViewEmail = (TextView) findViewById(R.id.textViewEmail);
        mButtonSignout = findViewById(R.id.btnSignout);

        mButtonSignout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                FirebaseAuth.getInstance().signOut();

                startActivity(new Intent( MainActivity.this, RegisterActivity.class));

             }
        });



        getUserInfo();

        // Para que funcione la navegacion entre fragments
        BottomNavigationView bottomNavegationView = findViewById(R.id.bnv_main);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavegationView,navController);
        // ----
    }



    private void getUserInfo(){
        String id= mAuth.getCurrentUser().getUid();
        if (!id.isEmpty()) {
            String email = mAuth.getCurrentUser().getEmail().toString();

            mTextViewEmail.setText(email);

        }
    }





}