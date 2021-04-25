package com.example.laspiedrasapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.laspiedrasapp.R;
import com.example.laspiedrasapp.databinding.FragmentNewProductCommerceBinding;
import com.example.laspiedrasapp.models.CommerceProductModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class NewProductCommerceFragment extends DialogFragment {

    private String PRODUCT_COMMERCE_COLLECTION = "shops_product"; // Nombre de la coleccion que tiene los productos de los comercios
    private String COMMERCE_COLLECTION = "shops"; // Nombre de la coleccion que tiene los datos de los comercios
    private String COMMERCE_PRODUCT_IMAGES_STORE = "commerce_product_images"; // Carpeta en Storage donde se guardan las imagenes de los productso


    private FragmentNewProductCommerceBinding binding;
    private FirebaseAuth mAuth; // Para poder obtener el id del usuario
    private DatabaseReference mDatabase; // Para extraer los datos de firebase
    private StorageReference storageReference; // Para el Storage
    private Uri resultUri; // Para la imagen
    private Boolean photoSelected=false;


    public NewProductCommerceFragment() {
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
        mAuth = FirebaseAuth.getInstance();// Inicializo el auth del usuario
        mDatabase = FirebaseDatabase.getInstance().getReference(); // Inicializo firebase
        storageReference = FirebaseStorage.getInstance().getReference().child(COMMERCE_PRODUCT_IMAGES_STORE); // Para guardar las fotos de perfil en storage
        return inflater.inflate(R.layout.fragment_new_product_commerce, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentNewProductCommerceBinding.bind(view);// Inicializo el binding
        String userId= mAuth.getCurrentUser().getUid(); // Obtengo el id del usuario logeado


        binding.ivCommerceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setRequestedSize(640,480)
                        .setAspectRatio(1,1).start(getContext(), NewProductCommerceFragment.this);
            }
        });

        binding.btnCommerceCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        binding.btnCommerceSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtengo los datos ingresados
                String product_name = binding.tvCommerceProductName.getText().toString();
                String product_price = binding.tvCommerceProductPrice.getText().toString();
                String product_category = binding.tvEditCommerceProductCategory.getText().toString();
                if( isValid(product_name,product_price,product_category) && photoSelected ) {// Me fijo que los datos sean validos
                    // Hay que ver si tiene internet y avisar
                    String key = mDatabase.child(PRODUCT_COMMERCE_COLLECTION).push().getKey(); // Obtengo el id del producto que voy a subir
                    // Creo los datos que se van a subir
                    CommerceProductModel commerceProductModel = new CommerceProductModel();// Creo el modelo del producto del comercio
                    commerceProductModel.setName(product_name);
                    commerceProductModel.setPrice(product_price);
                    commerceProductModel.setCategory(product_category);
                    commerceProductModel.setId(key);
                    commerceProductModel.setOwnerId(userId);
                    mDatabase.child(COMMERCE_COLLECTION).child(userId).child("products").child(key).setValue(true);// Guardo los datos en la coleccion con un identificador unico

                    StorageReference ref = storageReference.child(key);
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
                            commerceProductModel.setImgUrl(String.valueOf(downloaduri));
                            mDatabase.child(PRODUCT_COMMERCE_COLLECTION).child(key).setValue(commerceProductModel);// Guardo los datos en la coleccion con un identificador unico
                            dismiss();
                        }
                    });

                    dismiss();
                } else {
                    Toast.makeText(getContext(), "Debe completar todos los campos", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private boolean isValid(String product_name, String product_price, String product_category) {
        return !product_name.isEmpty() && !product_price.isEmpty() && !product_category.isEmpty();
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                photoSelected = true;
                resultUri = result.getUri();
                binding.ivCommerceImage.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}