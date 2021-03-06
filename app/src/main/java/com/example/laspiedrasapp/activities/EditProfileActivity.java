package com.example.laspiedrasapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.laspiedrasapp.R;
import com.example.laspiedrasapp.databinding.ActivityEditProfileBinding;
import com.example.laspiedrasapp.models.ProfileModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {
    private final String USERS_COLLECTIONS = MainActivity.getUsersCollections();
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private StorageReference storageReference; // Para el Storage

    private ActivityEditProfileBinding binding;
    private Uri resultUri = null;
    private Boolean verified=false;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        iniBinding();

        mDatabase = FirebaseDatabase.getInstance().getReference(); // Para la base de datos
        mAuth = FirebaseAuth.getInstance(); // Para obtener el ususario que esta logeado
        storageReference = FirebaseStorage.getInstance().getReference().child("user_prfile_images"); // Para guardar las fotos de perfil en storage

        userId = mAuth.getCurrentUser().getUid(); // Obtengo el id del usuario logeado


        // Recupero los datos del perfil del usuario y los coloco en los edit text
        mDatabase.child("users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
//                    Log.e("firebase", "Error getting data", task.getException());
                    Toast.makeText(EditProfileActivity.this,"Compruebe internet", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(task.getResult().exists()){ // Me fijo si el documento con userId existe (no es null)
                        ProfileModel profileModel = task.getResult().getValue(ProfileModel.class); // guardo la respuesta en un ProfileModel
                        binding.textInputEditTextName.setText(profileModel.getName());
                        binding.textInputEditTextPhone.setText(profileModel.getPhone());
                        Glide.with(EditProfileActivity.this).load(profileModel.getImgUrl()).into(binding.imgFoto); // Coloca la imagen en el imageview
                    }
                }
            }
        });
        //------


        // Guardo los datos ingresados por el usuario
        binding.tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Extraigo los datos ingresados de los ususarios
                String name = binding.textInputEditTextName.getText().toString();
                String phone = binding.textInputEditTextPhone.getText().toString();
                // Me fijo que los datos sean validos
                if( isValid(name,phone) ){
                    // Hay que ver si tiene internet y avisar
                    //---
                    ProfileModel profile = new ProfileModel(name,phone);// creo la clase Profile con los parametros
                    mDatabase.child(USERS_COLLECTIONS).child(userId).child("name").setValue(profile.getName());// Guardo los datos en la coleccion
                    mDatabase.child(USERS_COLLECTIONS).child(userId).child("phone").setValue(profile.getPhone());// Guardo los datos en la coleccion
                    if (resultUri!=null){
                        final StorageReference ref = storageReference.child(userId);
                        ref.putFile(resultUri).addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
                            profile.setImgUrl(String.valueOf(uri));
                            // Guardo los datos en el perfil del usuario
                            mDatabase.child(USERS_COLLECTIONS).child(userId).child("imgUrl").setValue(profile.getImgUrl());// Guardo los datos en la coleccion
                        }));
                    }
                    finish();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Debe completar todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //----

        // Vuelvo a la activity anterior sin hacer cambios
        binding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aca tengo que volver a la activity anterior
                finish();
            }
        });
        //----


        binding.btnSelefoto.setOnClickListener(new View.OnClickListener() { // Selecciono la imagen y me da la opcion de recortarla
            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity(EditProfileActivity.this);
            }
        });


        binding.btnVerifyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity(EditProfileActivity.this);
                verified=true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*
        Esta funcion es llamada luego de que el usuario elija la imagen
         */

        if (requestCode==CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Uri imageuri = CropImage.getPickImageResultUri(this, data);

            //Recortar Imagen
            CropImage.activity(imageuri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setRequestedSize(640,480)
                    .setAspectRatio(1,1).start(EditProfileActivity.this); // Esto es para dejar una forma por defecto
            // ---
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){ // Si la imagen fue recortada
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK){
                resultUri = result.getUri();
                if(verified){ // Si es la foto de verificacion
                    verified = false;
                    final StorageReference ref = storageReference.child(userId+"_verified");
                    ref.putFile(resultUri).addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Guardo los datos en el perfil del usuario
                        resultUri = null;
                        mDatabase.child(USERS_COLLECTIONS).child(userId).child("verified_photo").setValue(String.valueOf(uri));// Guardo los datos en la coleccion
                        mDatabase.child(USERS_COLLECTIONS).child(userId).child("verified").setValue(true);// Guardo los datos en la coleccion
                    }));
                } else { // Si es la otra foto
                    File url = new File(resultUri.getPath());
                    Picasso.with(this).load(url).into(binding.imgFoto); // Coloca la imagen en el imageview
                }

                // Hasta aca se tiene guardada la imagen en url, lista para subir a store
            }
        }
    }

    private void uploadImage(String name){
//                        loading.setTitle("Subiendo Foto...");
//                        loading.setMessage("Espere por favor...");
//                        loading.show();

                        final StorageReference ref = storageReference.child(name);
                        UploadTask uploadTask = ref.putFile(resultUri);

                        //subir imagen en storage
                        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()){
                                    throw Objects.requireNonNull(task.getException());
                                }
                                return ref.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Uri downloaduri = task.getResult(); // Url de la foto
                                Toast.makeText(EditProfileActivity.this,"Imagen guardada con exito", Toast.LENGTH_LONG).show();

                            }
                        });

    }

    private boolean isValid(String name, String phone) {
        // Para evaluar que los datos dados son validos
        return !name.isEmpty() && !phone.isEmpty();
    }

    private void iniBinding() {
        // Para inicializar el Binding
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}