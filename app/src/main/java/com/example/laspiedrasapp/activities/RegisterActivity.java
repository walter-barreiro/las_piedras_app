package com.example.laspiedrasapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.laspiedrasapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class RegisterActivity extends AppCompatActivity {

    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    private Button mButtonRegister;
    private TextView mAlredyHaveAccount;
    private GoogleSignInClient mGoogleSignInClient;
    public  SignInButton mButtonSignIn;


    //VARIABLE DE LOS DATOS QUE VAMOS A REGISTRAR
    private String email = "";
    private String password = "";
    private FirebaseAuth mAuth;
    public static final int RC_SIGN_IN = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

     //INSTANCIAMOS FirebaseAuth EN "mAuth"
        mAuth = FirebaseAuth.getInstance();
     //SE REFERENCIAN  LOS EDIT TEXT DEL LAYOUT CON SUS CORRESPONDIENTES VARIABLES
        mEditTextEmail = (EditText) findViewById(R.id.textInputEditTextCorreo);
        mEditTextPassword = (EditText) findViewById(R.id.textInputEditTextContrasena);
        mButtonRegister = (Button) findViewById(R.id.btnRegister);
        mAlredyHaveAccount = (TextView) findViewById(R.id.alredyHaveAccount);
        mButtonSignIn = findViewById(R.id.btnSignin);



     //SE ESTABLECE LO QUE HACE EL BOTON  mButtonRegister
        mButtonRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                email = mEditTextEmail.getText().toString();
                password = mEditTextPassword.getText().toString();

                if (!email.isEmpty() &&!password.isEmpty()){
                    if (password.length() >= 6){
                        registerUser();
                    }
                    else{
                        Toast.makeText(RegisterActivity.this, "Password deben contener mas de 5 caracteres ", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Debe completar los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
     // BOTON QUE ME LLEVA A LoginActivity PARA LOGUEAR ("mButtonSendToLogin")
        mAlredyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                //finish();
            }
        });

        //---------------------------------------------------------------------------------------//
        //---------------------------------------------------------------------------------------//
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
        .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
         mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton googleLogin = findViewById(R.id.btnSignin);
        googleLogin.setOnClickListener(handleGoogleLogin);



    }//FIN DE onCreate()

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private View.OnClickListener handleGoogleLogin = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            signIn();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                // ...
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Sorry authentication failed ", Toast.LENGTH_SHORT).show();

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
    //---------------------------------------------------------------------------------------//
    //---------------------------------------------------------------------------------------//

    private void registerUser(){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task){
                if (task.isSuccessful()){
                    //Map<String, Object> map = new HashMap<>();
                    //map.put("email", email);prueba@
                    //map.put("password", password);

                    String id = mAuth.getCurrentUser().getUid();
                    if (id != null){
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        finish();
                    }
                    else{
                        Toast.makeText( RegisterActivity.this, "No se pudo crear los datos correctamente", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                    dameToastdeerror(errorCode);
                    //Toast.makeText( RegisterActivity.this, "No se pudo registrar este usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void dameToastdeerror(String error) {

        switch (error) {

            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(RegisterActivity.this, "El formato del token personalizado es incorrecto. Por favor revise la documentaci??n", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(RegisterActivity.this, "El token personalizado corresponde a una audiencia diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(RegisterActivity.this, "La credencial de autenticaci??n proporcionada tiene un formato incorrecto o ha caducado.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_EMAIL":
                Toast.makeText(RegisterActivity.this, "La direcci??n de correo electr??nico est?? mal formateada.", Toast.LENGTH_LONG).show();
                mEditTextEmail.setError("La direcci??n de correo electr??nico est?? mal formateada.");
                mEditTextEmail.requestFocus();
                break;

            case "ERROR_WRONG_PASSWORD":
                Toast.makeText(RegisterActivity.this, "La contrase??a no es v??lida o el usuario no tiene contrase??a.", Toast.LENGTH_LONG).show();
                mEditTextPassword.setError("la contrase??a es incorrecta ");
                mEditTextPassword.requestFocus();
                mEditTextPassword.setText("");
                break;

            case "ERROR_USER_MISMATCH":
                Toast.makeText(RegisterActivity.this, "Las credenciales proporcionadas no corresponden al usuario que inici?? sesi??n anteriormente..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(RegisterActivity.this,"Esta operaci??n es sensible y requiere autenticaci??n reciente. Inicie sesi??n nuevamente antes de volver a intentar esta solicitud.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(RegisterActivity.this, "Ya existe una cuenta con la misma direcci??n de correo electr??nico pero diferentes credenciales de inicio de sesi??n. Inicie sesi??n con un proveedor asociado a esta direcci??n de correo electr??nico.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                Toast.makeText(RegisterActivity.this, "La direcci??n de correo electr??nico ya est?? siendo utilizada por otra cuenta..   ", Toast.LENGTH_LONG).show();
                mEditTextEmail.setError("La direcci??n de correo electr??nico ya est?? siendo utilizada por otra cuenta.");
                mEditTextEmail.requestFocus();
                break;

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(RegisterActivity.this, "Esta credencial ya est?? asociada con una cuenta de usuario diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_DISABLED":
                Toast.makeText(RegisterActivity.this, "La cuenta de usuario ha sido inhabilitada por un administrador..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_TOKEN_EXPIRED":
                Toast.makeText(RegisterActivity.this, "La credencial del usuario ya no es v??lida. El usuario debe iniciar sesi??n nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(RegisterActivity.this, "No hay ning??n registro de usuario que corresponda a este identificador. Es posible que se haya eliminado al usuario.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(RegisterActivity.this, "La credencial del usuario ya no es v??lida. El usuario debe iniciar sesi??n nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(RegisterActivity.this, "Esta operaci??n no est?? permitida. Debes habilitar este servicio en la consola.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_WEAK_PASSWORD":
                Toast.makeText(RegisterActivity.this, "La contrase??a proporcionada no es v??lida..", Toast.LENGTH_LONG).show();
                mEditTextPassword.setError("La contrase??a no es v??lida, debe tener al menos 6 caracteres");
                mEditTextPassword.requestFocus();
                break;

        }

    }





}