package com.example.laspiedrasapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.laspiedrasapp.R;
import com.example.laspiedrasapp.databinding.FragmentEditProductProfileBinding;
import com.example.laspiedrasapp.models.ProfileProductModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import static android.app.Activity.RESULT_OK;

public class EditProductProfileFragment extends DialogFragment {

    private FirebaseAuth mAuth; // Para poder obtener el id del usuario
    private DatabaseReference mDatabase; // Para extraer los datos de firebase
    private StorageReference storageReference; // Para el Storage
    private FragmentEditProductProfileBinding binding;
    private ProfileProductModel profileProductModel;
    private Uri resultUri;
    private String productId;
    private String userId;
    private Boolean photoChanged = false;

    public EditProductProfileFragment() {
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
        View root = inflater.inflate(R.layout.fragment_edit_product_profile, container, false);
        mAuth = FirebaseAuth.getInstance();// Inicializo el auth del usuario
        mDatabase = FirebaseDatabase.getInstance().getReference(); // Inicializo firebase
        storageReference = FirebaseStorage.getInstance().getReference().child("user_product_images"); // Para guardar las fotos de perfil en storage
        // Recupero el producto
        Bundle bundle = this.getArguments();
        profileProductModel = (ProfileProductModel) bundle.getSerializable("product");
        // ---


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentEditProductProfileBinding.bind(view);// Inicializo el binding

        binding.tvEditProductName.setText(profileProductModel.getProduct_name());
        binding.tvEditProductPrice.setText(profileProductModel.getProduct_price());
        binding.tvEditProductCategory.setText(profileProductModel.getProduct_category());
        Glide.with(getContext()).load(profileProductModel.getProduct_image_url()).into(binding.ivEdit);

        productId = profileProductModel.getProduct_id();
        userId= mAuth.getCurrentUser().getUid(); // Obtengo el id del usuario logeado

//        Picasso.with(getContext()).load(profileProductModel.getProduct_image_url()).into(binding.ivEdit);

        binding.btnEditCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.btnEditSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Extraigo los datos del producto
                String product_name = binding.tvEditProductName.getText().toString();
                String product_price = binding.tvEditProductPrice.getText().toString();
                String product_category = binding.tvEditProductCategory.getText().toString();
                // Me fijo que los datos sean validos
                if (isValid(product_name,product_price, product_category)){
                    // Hay que ver si tiene internet y avisar
                    //---
//                    uploadImage(key);
                    // Creo los datos que se van a subir
                    mDatabase.child("products").child(productId).child("product_name").setValue(product_name);// Guardo los datos en la coleccion con un identificador unico
                    mDatabase.child("products").child(productId).child("product_price").setValue(product_price);// Guardo los datos en la coleccion con un identificador unico
                    mDatabase.child("products").child(productId).child("product_category").setValue(product_category);
                    if(photoChanged){
                        final StorageReference ref = storageReference.child(productId);
                        ref.putFile(resultUri).addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
                            mDatabase.child("products").child(productId).child("product_image_url").setValue(String.valueOf(uri));// Guardo los datos en la coleccion con un identificador unico
                        }));
                    }
                    dismiss();
                } else {
                    // Mostar algun mensaje de error
                }

            }
        });

        binding.tvEditProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setRequestedSize(640,480)
                        .setAspectRatio(1,1).start(getContext(), EditProductProfileFragment.this);
            }
        });

        binding.btnEditDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("products").child(productId).removeValue();
                mDatabase.child("users").child(userId).child("products").child(productId).removeValue();
                dismiss();
            }
        });

    }

    private boolean isValid(String product_name, String product_price, String product_category) {
        return true;
    }

    /*private boolean isValid(String productName, String product_name, String product_price, String product_category) {
        return true;
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                binding.ivEdit.setImageURI(resultUri);
                photoChanged = true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}