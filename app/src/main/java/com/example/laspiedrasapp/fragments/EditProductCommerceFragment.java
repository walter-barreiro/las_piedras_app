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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.laspiedrasapp.R;
import com.example.laspiedrasapp.databinding.FragmentEditProductCommerceBinding;
import com.example.laspiedrasapp.models.CommerceProductModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import static android.app.Activity.RESULT_OK;

public class EditProductCommerceFragment extends DialogFragment {
    private static final String PRODUCT_COLLECTION = "shops_product";
    private FirebaseAuth mAuth; // Para poder obtener el id del usuario
    private DatabaseReference mDatabase; // Para extraer los datos de firebase
    private StorageReference storageReference; // Para el Storage
    private CommerceProductModel commerceProductModel;
    private Uri resultUri;
    private String productId;
    private String userId;
    private Boolean photoChanged = false;

    private FragmentEditProductCommerceBinding binding;

    public EditProductCommerceFragment() {
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
        storageReference = FirebaseStorage.getInstance().getReference().child("commerce_product_images"); // Para guardar las fotos de perfil en storage
        // Recupero el producto
        Bundle bundle = this.getArguments();
        commerceProductModel = (CommerceProductModel) bundle.getSerializable("product_commerce");
        // ---
        return inflater.inflate(R.layout.fragment_edit_product_commerce, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentEditProductCommerceBinding.bind(view);

        binding.tvEditProductCommerceName.setText(commerceProductModel.getName());
        binding.tvEditProductCommercePrice.setText(commerceProductModel.getPrice());
        binding.tvEditProductCategory.setText(commerceProductModel.getCategory());
        Glide.with(getContext()).load(commerceProductModel.getImgUrl()).into(binding.ivEditProductCommrece);

        productId = commerceProductModel.getId();
        userId= mAuth.getCurrentUser().getUid();

        binding.btnEditCommerceCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.tvEditProductCommerceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setRequestedSize(640,480)
                        .setAspectRatio(1,1).start(getContext(), EditProductCommerceFragment.this);
            }
        });

        binding.btnEditCommerceSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtengo los datos ingresados
                String product_name = binding.tvEditProductCommerceName.getText().toString();
                String product_price = binding.tvEditProductCommercePrice.getText().toString();
                String product_category = binding.tvEditProductCategory.getText().toString();
                if( isValid(product_name,product_price, product_category) ){// Me fijo que los datos sean validos
                    // Hay que ver si tiene internet y avisar
                    mDatabase.child(PRODUCT_COLLECTION).child(productId).child("name").setValue(product_name);// Guardo los datos en la coleccion con un identificador unico
                    mDatabase.child(PRODUCT_COLLECTION).child(productId).child("price").setValue(product_price);// Guardo los datos en la coleccion con un identificador unico
                    mDatabase.child(PRODUCT_COLLECTION).child(productId).child("category").setValue(product_category);
                    if(photoChanged){
                        final StorageReference ref = storageReference.child(productId);
                        ref.putFile(resultUri).addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
                            mDatabase.child(PRODUCT_COLLECTION).child(productId).child("imgUrl").setValue(String.valueOf(uri));// Guardo la url de la foto del producto
                        }));
                    }
                    dismiss();
                } else {
                    // ToDo Mostrar algun mensaje de error
                }

            }
        });

        binding.btnEditCommerceDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child(PRODUCT_COLLECTION).child(productId).removeValue();
                mDatabase.child("shops").child(userId).child("products").child(productId).removeValue();
                dismiss();

            }
        });
    }

    private boolean isValid(String product_name, String product_price, String product_category) {
        return !product_name.isEmpty() && !product_price.isEmpty() && product_category.isEmpty() ;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                binding.ivEditProductCommrece.setImageURI(resultUri);
                photoChanged = true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}