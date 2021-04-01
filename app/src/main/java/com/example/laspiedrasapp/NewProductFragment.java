package com.example.laspiedrasapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laspiedrasapp.databinding.FragmentNewProductBinding;
import com.example.laspiedrasapp.databinding.FragmentProfileBinding;
import com.example.laspiedrasapp.models.ProfileModel;
import com.example.laspiedrasapp.models.ProfileProductModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewProductFragment extends DialogFragment {
    private FirebaseAuth mAuth; // Para poder obtener el id del usuario
    private DatabaseReference mDatabase; // Para extraer los datos de firebase
    private FragmentNewProductBinding binding;

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



        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentNewProductBinding.bind(view);// Inicializo el binding



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
                    ProfileProductModel profileProductModel = new ProfileProductModel();// creo la clase Profile con los parametros
                    profileProductModel.setProduct_name(product_name);
                    profileProductModel.setProduct_price(product_price);
                    String productId = "2";
                    mDatabase.child("products").child(productId).setValue(profileProductModel);// Guardo los datos en la coleccion
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


    }

    private boolean isValid(String product_name, String product_price) {
        return true;
    }
}