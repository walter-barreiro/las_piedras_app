package com.example.laspiedrasapp;

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

import com.example.laspiedrasapp.databinding.FragmentNewProductBinding;
import com.example.laspiedrasapp.models.ProfileProductModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class NewProductFragment extends  DialogFragment {
    private FirebaseAuth mAuth; // Para poder obtener el id del usuario
    private DatabaseReference mDatabase; // Para extraer los datos de firebase
    private FragmentNewProductBinding binding;
    private StorageReference storageReference; // Para el Storage

    private Uri resultUri; // Para la imagen

    // Para la imagen
    private static final int REQUEST_IMAGE_GALERY = 101; // https://www.youtube.com/watch?v=Zj6ZXmIa8xI

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
                    // Creo los datos que se van a subir
                    ProfileProductModel profileProductModel = new ProfileProductModel();// creo la clase Profile con los parametros
                    profileProductModel.setProduct_name(product_name);
                    profileProductModel.setProduct_price(product_price);
                    profileProductModel.setProduct_id(key);
                    profileProductModel.setProduct_userId(userId);
                    uploadImage(key);
                    //----
                    mDatabase.child("products").child(key).setValue(profileProductModel);// Guardo los datos en la coleccion con un identificador unico
                    // Guardo el id del producto en la base de datos del usuario
                    mDatabase.child("users").child(userId).child("products").child(key).setValue(true);// Guardo los datos en la coleccion con un identificador unico
                    // Ahora tengo que salir del dialog fragment
                    dismiss();

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
//                pedirPerisos();
                openGalery();
            }
        });
        //----

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // https://www.youtube.com/watch?v=Zj6ZXmIa8xI
        super.onActivityResult(requestCode, resultCode, data);
        resultUri = data.getData();
        binding.imageView3.setImageURI(resultUri);
    }

    private void pedirPerisos() {
    }

    private void openGalery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_IMAGE_GALERY );
    }

    private boolean isValid(String product_name, String product_price) {
        return true;
    }

    private void uploadImage(String name){
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
//                Toast.makeText(getContext(),"Imagen guardada con exito"+downloaduri.toString(), Toast.LENGTH_LONG).show();
            }
        });


    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Toast.makeText(getContext(), "onActivityResult", Toast.LENGTH_SHORT).show();
//        /*
//        Esta funcion es llamada luego de que el usuario elija la imagen
//         */
//
//        if (requestCode==CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK){
//            Uri imageuri = CropImage.getPickImageResultUri(getContext(), data);
//
//            //Recortar Imagen
//            CropImage.activity(imageuri)
//                    .setGuidelines(CropImageView.Guidelines.ON)
//                    .setRequestedSize(640,480)
//                    .setAspectRatio(1,1).start(getActivity()); // Esto es para dejar una forma por defecto
//            // ---
//        }
//
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){ // Si la imagen fue recortada
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//
//            if (resultCode == getActivity().RESULT_OK){
//                resultUri = result.getUri();
//
//                File url = new File(resultUri.getPath());
//                Picasso.with(getActivity()).load(url).into(binding.imageView3); // Coloca la imagen en el imageview
//
//                // Hasta aca se tiene guardada la imagen en url, lista para subir a store
//            }
//        }
//    }
}