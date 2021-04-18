package com.example.laspiedrasapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText mEditTexEmail;
    private EditText mEditTextPassword;
    private Button mButtonLogin;
    private TextView mTextViewResetPassword;

    //VARIABLE DE LOS DATOS QUE VAMOS A LOGUEAR
    private String email = "";
    private String password = "";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mEditTexEmail = (EditText) findViewById(R.id.editTextEmail);
        mEditTextPassword = (EditText) findViewById(R.id.editTextPassword);
        mButtonLogin = (Button) findViewById(R.id.btnLogin);
        mTextViewResetPassword = (TextView) findViewById(R.id.forgotPassword);



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
}