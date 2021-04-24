package com.example.laspiedrasapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.laspiedrasapp.databinding.ActivityEditBusinessBinding;
import com.example.laspiedrasapp.models.BusinessModel;
import com.example.laspiedrasapp.models.ProfileModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

        initFirebase();

        Intent intent = this.getIntent();
        business = (BusinessModel) intent.getSerializableExtra("business");
        if(business!=null){
            setValues();
        }

        binding.tvBusinessSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String profession = binding.tinputEditTextProfession.getText().toString();
                String location = binding.textInputEditTextUbication.getText().toString();
                String description = binding.textInputEditTextDescription1.getText().toString();
                String name = binding.textInputEditTextName1.getText().toString();

                if(isDataValid(profession,location,description,name)){
//                    BusinessModel businessModel = new BusinessModel();// creo la clase

                    mDatabase.child(BUSINESS_COLLECTION).child(userId).child("profession").setValue(profession);
                    mDatabase.child(BUSINESS_COLLECTION).child(userId).child("id").setValue(userId);
                    mDatabase.child(BUSINESS_COLLECTION).child(userId).child("name").setValue(name);
                    mDatabase.child(BUSINESS_COLLECTION).child(userId).child("description").setValue(description);
                    mDatabase.child(BUSINESS_COLLECTION).child(userId).child("location").setValue(location);

                    // Guardo la foto
                    if (resultUri!=null){
                        final StorageReference ref = storageReference.child(userId);
                        ref.putFile(resultUri).addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
//                        businessModel.setImgUrl(String.valueOf(uri));
                            // Guardo los datos en el perfil del usuario
                            mDatabase.child(BUSINESS_COLLECTION).child(userId).child("imgUrl").setValue(String.valueOf(uri));// Guardo los datos en la coleccion
                        }));

                    }
                    finish();
                } else {
                    Toast.makeText(EditBusinessActivity.this, "Debe completar todos los campos", Toast.LENGTH_SHORT).show();
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
        if(!business.getProfession().isEmpty()){
            binding.tinputEditTextProfession.setText(business.getProfession());
        }
        if(!business.getName().isEmpty()) {
            binding.textInputEditTextName1.setText(business.getName());
        }
        if(!business.getDescription().isEmpty()) {
            binding.textInputEditTextDescription1.setText(business.getDescription());
        }
        if(!business.getLocation().isEmpty()) {
            binding.textInputEditTextUbication.setText(business.getLocation());
        }
        if(!business.getImgUrl().isEmpty()) {
            Glide.with(EditBusinessActivity.this).load(business.getImgUrl()).into(binding.ivEditBusimessImage); // Coloca la imagen en el imageview
        }
    }
    private void initFirebase(){
        mAuth = FirebaseAuth.getInstance();// Inicializo el auth del usuario
        mDatabase = FirebaseDatabase.getInstance().getReference(); // Inicializo firebase
        storageReference = FirebaseStorage.getInstance().getReference().child("business_profile_photo"); // Para guardar las fotos de perfil en storage
        userId= mAuth.getCurrentUser().getUid(); // Obtengo el id del usuario logeado
    }

    public boolean isDataValid(String profession, String location, String description,String name){
        return !profession.isEmpty() && !location.isEmpty() && !description.isEmpty() && !name.isEmpty();
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