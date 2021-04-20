package com.example.laspiedrasapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.laspiedrasapp.R;
import com.example.laspiedrasapp.databinding.FragmentNewProductBinding;
import com.example.laspiedrasapp.models.ProfileProductModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import static android.app.Activity.RESULT_OK;

public class NewProductFragment extends  DialogFragment {
    private FirebaseAuth mAuth; // Para poder obtener el id del usuario
    private DatabaseReference mDatabase; // Para extraer los datos de firebase
    private FragmentNewProductBinding binding;
    private StorageReference storageReference; // Para el Storage
    private Uri resultUri; // Para la imagen

    public NewProductFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_new_product, container, false);
        mAuth = FirebaseAuth.getInstance();// Inicializo el auth del usuario
        mDatabase = FirebaseDatabase.getInstance().getReference(); // Inicializo firebase
        storageReference = FirebaseStorage.getInstance().getReference().child("user_product_images"); // Para guardar las fotos de perfil en storage

        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentNewProductBinding.bind(view);// Inicializo el binding
        String userId= mAuth.getCurrentUser().getUid(); // Obtengo el id del usuario logeado

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Extraigo los datos del producto
                String product_name = binding.tvProductName.getText().toString();
                String product_price = binding.tvProductPrice.getText().toString();
                // Me fijo que los datos sean validos
                if( isValid(product_name,product_price) ){
                    // Hay que ver si tiene internet y avisar
                    //---
                    String key = mDatabase.child("products").push().getKey(); // Obtengo el id del producto que voy a subir
//                    uploadImage(key);
                    // Creo los datos que se van a subir
                    ProfileProductModel profileProductModel = new ProfileProductModel();// creo la clase Profile con los parametros
                    final StorageReference ref = storageReference.child(key);
                    ref.putFile(resultUri).addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
                        profileProductModel.setProduct_image_url(String.valueOf(uri));
                        profileProductModel.setProduct_name(product_name);
                        profileProductModel.setProduct_price(product_price);
                        profileProductModel.setProduct_id(key);
                        profileProductModel.setProduct_userId(userId);
                        mDatabase.child("products").child(key).setValue(profileProductModel);// Guardo los datos en la coleccion con un identificador unico
                        //----
                        // Guardo el id del producto en la base de datos del usuario
                        mDatabase.child("users").child(userId).child("products").child(key).setValue(true);// Guardo los datos en la coleccion con un identificador unico
                        // Ahora tengo que salir del dialog fragment
                        dismiss();
                    }));
                } else {
                    // Mostar algun mensaje de error
                }
            }
        });
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        // Para obtener la imagen
        binding.tvProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                        .setRequestedSize(640,480)
                        .setAspectRatio(1,1).start(getContext(), NewProductFragment.this);
            }
        });
        //----
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                binding.imageView3.setImageURI(resultUri);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private boolean isValid(String product_name, String product_price) {
        return true;
    }

}