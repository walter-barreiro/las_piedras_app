package com.example.laspiedrasapp.activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laspiedrasapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText mEditTexEmail;
    private EditText mEditTextPassword;
    private Button mButtonLogin;
    private TextView mTextViewResetPassword;
    private TextView mTextViewSignUp;

    //VARIABLE DE LOS DATOS QUE VAMOS A LOGUEAR
    private String email = "";
    private String password = "";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mEditTexEmail = (EditText) findViewById(R.id.textInputEditTextCorreo);
        mEditTextPassword = (EditText) findViewById(R.id.textInputEditTextContrasena);
        mButtonLogin = (Button) findViewById(R.id.btnLogin);
        mTextViewResetPassword = (TextView) findViewById(R.id.forgotPassword);
        mTextViewSignUp = (TextView) findViewById(R.id.textViewSignUp);

        mTextViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        mButtonLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                email = mEditTexEmail.getText().toString();
                password = mEditTextPassword.getText().toString();

                if (!email.isEmpty() &&!password.isEmpty()){
                    loginUser();
                }
                else{
                    Toast.makeText(LoginActivity.this, "Debe completar los campos", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mTextViewResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this , ResetPasswordActivity.class));
            }
        });
    }

    private void loginUser(){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
                else{
                    Toast.makeText( LoginActivity.this, "No se pudo iniciar sesion, comprueba los datos", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    protected void onStart() {

        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }
    }
}