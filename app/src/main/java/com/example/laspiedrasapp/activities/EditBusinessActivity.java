package com.example.laspiedrasapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.example.laspiedrasapp.databinding.ActivityEditBusinessBinding;
import com.example.laspiedrasapp.models.BusinessModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class EditBusinessActivity extends AppCompatActivity {
    private FirebaseAuth mAuth; // Para poder obtener el id del usuario
    private DatabaseReference mDatabase; // Para extraer los datos de firebase
    private StorageReference storageReference;
    private final String BUSINESS_COLLECTION = "business";

    private ActivityEditBusinessBinding binding;
    private BusinessModel business;

    private String userId;
    private Uri resultUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditBusinessBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();// Inicializo el auth del usuario
        mDatabase = FirebaseDatabase.getInstance().getReference(); // Inicializo firebase
        storageReference = FirebaseStorage.getInstance().getReference().child("business_profile_photo"); // Para guardar las fotos de perfil en storage
        userId= mAuth.getCurrentUser().getUid(); // Obtengo el id del usuario logeado

        Intent intent = this.getIntent();
        business = (BusinessModel) intent.getSerializableExtra("business");
        setValues();
        Toast.makeText(this,business.getProfession(), Toast.LENGTH_SHORT).show();

        // ToDo recuperar datos del perfil de usuario y colocarlos en los editText y la imagen

        binding.tvBusinessSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditBusinessActivity.this, "Guardar", Toast.LENGTH_SHORT).show();
                // ToDo guardar los datos en BUSINESS_COLLECTION y agregar business=created en USERS_PROFILE
                // Extraigo los datos ingresados de los ususarios
                String profession = binding.tinputEditTextProfession.getText().toString();
                // Me fijo que los datos sean validos
                if( isDataValid() ){
                    // Hay que ver si tiene internet y avisar
                    //---
//                    BusinessModel businessModel = new BusinessModel();// creo la clase
                    mDatabase.child(BUSINESS_COLLECTION).child(userId).child("profession").setValue(profession);// Guardo los datos en la coleccion
                    mDatabase.child(BUSINESS_COLLECTION).child(userId).child("id").setValue(userId);// Guardo los datos en la coleccion

                    if (resultUri!=null){
                        final StorageReference ref = storageReference.child(userId);
                        ref.putFile(resultUri).addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
//                        businessModel.setImgUrl(String.valueOf(uri));
                            // Guardo los datos en el perfil del usuario
                            mDatabase.child(BUSINESS_COLLECTION).child(userId).child("imgUrl").setValue(String.valueOf(uri));// Guardo los datos en la coleccion
                        }));

                    }
                    // Ahora tengo que salir de la actividad
                    finish();

                } else {
                    // Mostar algun mensaje de error
                }
            }
        });

        binding.tvBusinessCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.ivEditBusimessImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setRequestedSize(640,480)
                        .setAspectRatio(1,1).start(EditBusinessActivity.this);
            }
        });

    }

    private void setValues() {

        binding.tinputEditTextProfession.setText(business.getProfession());
    }

    public boolean isDataValid(){
        return true;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                binding.ivEditBusimessImage.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}