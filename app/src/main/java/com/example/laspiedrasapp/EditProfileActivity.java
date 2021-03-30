package com.example.laspiedrasapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.laspiedrasapp.databinding.ActivityEditProfileBinding;
import com.example.laspiedrasapp.models.ProfileModel;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private ActivityEditProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        iniBinding();

        mDatabase = FirebaseDatabase.getInstance().getReference(); // Para la base de datos
        mAuth = FirebaseAuth.getInstance(); // Para obtener el ususario que esta logeado

        String userId = mAuth.getCurrentUser().getUid(); // Obtengo el id del usuario logeado

        // Recupero los datos del perfil del usuario y los coloco en los edit text
        mDatabase.child("users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                    Toast.makeText(EditProfileActivity.this,"Compruebe internet", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(task.getResult().exists()){ // Me fijo si el documento con userId existe (no es null)
                        ProfileModel profileModel = task.getResult().getValue(ProfileModel.class); // guardo la respuesta en un ProfileModel
                        binding.etName.setText(profileModel.getName());
                        binding.etPhone.setText(profileModel.getPhone());
                    }
                }
            }
        });

        // Guardo los datos ingresados por el usuario
        binding.tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Extraigo los datos ingresados de los ususarios
                String name = binding.etName.getText().toString();
                String phone = binding.etPhone.getText().toString();
                // Me fijo que los datos sean validos
                if( isValid(name,phone) ){
                    // Hay que ver si tiene internet y avisar
                    //---
                    ProfileModel profile = new ProfileModel(name,phone);// creo la clase Profile con los parametros
                    mDatabase.child("users").child(userId).setValue(profile);// Guardo los datos en la coleccion
                    // Ahora tengo que salir de la actividad

                } else {
                    // Mostar algun mensaje de error
                }
            }
        });

        // Vuelvo a la activity anterior sin hacer cambios
        binding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aca tengo que volver a la activity anterior
            }
        });
    }

    private boolean isValid(String name, String phone) {
        // Para evaluar que los datos dados son validos
        return true;
    }

    private void iniBinding() {
        // Para inicializar el Binding
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }
}