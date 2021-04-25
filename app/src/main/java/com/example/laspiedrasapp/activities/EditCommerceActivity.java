package com.example.laspiedrasapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.laspiedrasapp.databinding.ActivityEditCommerceBinding;
import com.example.laspiedrasapp.models.BusinessModel;
import com.example.laspiedrasapp.models.CommerceModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditCommerceActivity extends AppCompatActivity {
    private FirebaseAuth mAuth; // Para poder obtener el id del usuario
    private DatabaseReference mDatabase; // Para extraer los datos de firebase
    private StorageReference storageReference;
    ActivityEditCommerceBinding binding;
    private FusedLocationProviderClient fusedLocationClient;
    private CommerceModel commerceModel;
    private Map<String,Object> latlang = new HashMap<>();
    private StorageReference ref;
    private ProgressDialog progressDialog;



    private String userId;
    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditCommerceBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();// Inicializo el auth del usuario
        mDatabase = FirebaseDatabase.getInstance().getReference(); // Inicializo firebase
        storageReference = FirebaseStorage.getInstance().getReference(); // Para guardar las fotos de perfil en storage
        userId= mAuth.getCurrentUser().getUid(); // Obtengo el id del usuario logeado
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        progressDialog = new ProgressDialog(EditCommerceActivity.this);

        setContentView(view);


        Intent intent = this.getIntent();
        commerceModel = (CommerceModel) intent.getSerializableExtra("commerceModel");
        if(commerceModel!=null){
            setValues();
        }




        binding.tvEditCommerceSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCommcerce();
            }
        });

        binding.tvEditCommerceBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity(EditCommerceActivity.this);
            }
        });

        binding.tvEditCommerceCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnEditCommerceAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subirLatLongFirebase();
            }
        });


    }

    private void setValues() {
        binding.tinputCommcerceName.setText(commerceModel.getName());
        binding.tinputCommcerceDescription.setText(commerceModel.getDescription());
        Glide.with(EditCommerceActivity.this).load(commerceModel.getBanner_url()).into(binding.ivEditCommcerceBanner); // Coloca la imagen en el imageview


    }

    private void saveCommcerce() {
        // Extraigo los datos ingresados de los ususarios
        String name = binding.tinputCommcerceName.getText().toString();
        String description = binding.tinputCommcerceDescription.getText().toString();
        //---

        progressDialog.show();
        if( isValid(name,description) ){ // Me fijo que los datos sean validos
            // Hay que ver si tiene internet y avisar
            //---
            if(!latlang.isEmpty()){
                mDatabase.child("shops").child(userId).child("coordinates").setValue(latlang);
            }
            mDatabase.child("shops").child(userId).child("name").setValue(name);// Guardo los datos en la coleccion
            mDatabase.child("shops").child(userId).child("ownerId").setValue(userId);// Guardo los datos en la coleccion
            mDatabase.child("shops").child(userId).child("description").setValue(description);// Guardo los datos en la coleccion
            mDatabase.child("users").child(userId).child("commerce").setValue("created");
            // Guardo el  banner si fue cambiado o agregado
            if (resultUri!=null){
                ref = storageReference.child("shops_banners").child(userId);
                UploadTask uploadTask = ref.putFile(resultUri);
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
                        mDatabase.child("shops").child(userId).child("banner_url").setValue(String.valueOf(downloaduri));// Guardo los datos en la coleccion
                        finish();
                    }
                });

            } else {
                finish();
            }
        } else {
            Toast.makeText(this, "Debe completar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValid(String name,String description) {

        return !name.isEmpty() && !description.isEmpty() ;
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
                    .setAspectRatio(2,1).start(EditCommerceActivity.this); // Esto es para dejar una forma por defecto
            // ---
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){ // Si la imagen fue recortada
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                resultUri = result.getUri();
                Glide.with(EditCommerceActivity.this).load(resultUri).into(binding.ivEditCommcerceBanner); // Coloca la imagen en el imageview
            }
        }
    }

    private void subirLatLongFirebase() {
        // Compruebo los permisos y pido que los prendan
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EditCommerceActivity.this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION},1);

            return;
        }
        //Obtengo las coordenadas y las guardo en la base de datos
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Log.e("Latitud: ", + location.getLatitude() + "Longitud: " + location.getLongitude());

                            latlang.put("latitud",location.getLatitude());
                            latlang.put("longitud", location.getLongitude());

                            Toast.makeText(EditCommerceActivity.this, "Uicación guardada: ", Toast.LENGTH_LONG).show();;
                        }
                    }
                });
    }

}