package com.example.laspiedrasapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.laspiedrasapp.databinding.ActivityCommerceBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CommerceActivity extends AppCompatActivity {

    private ActivityCommerceBinding binding;
    private FirebaseAuth mAuth; // Para poder obtener el id del usuario
    private DatabaseReference mDatabase; // Para extraer los datos de firebase
    private StorageReference storageReference;

    private String userId;
    private Uri resultUri;
    CommerceViewPagerAdapter commerceViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();// Inicializo el auth del usuario
        mDatabase = FirebaseDatabase.getInstance().getReference(); // Inicializo firebase
        storageReference = FirebaseStorage.getInstance().getReference(); // Para guardar las fotos de perfil en storage

        userId= mAuth.getCurrentUser().getUid(); // Obtengo el id del usuario logeado

        binding = ActivityCommerceBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setValues();// Obtengo los datos de firebase y los coloco en los view
        initTabLayoutViewPager2();// Para la navegacion entre Productos y reseñas

        binding.btnCommerceEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommerceActivity.this,EditCommerceActivity.class);
                startActivity(intent); // Para ir al registro
            }
        });


    }

    private void initTabLayoutViewPager2() {
        commerceViewPagerAdapter = new CommerceViewPagerAdapter(getSupportFragmentManager(),getLifecycle());
        commerceViewPagerAdapter.addFragment(new ProductsCommerceFragment());
        commerceViewPagerAdapter.addFragment(new ReviewsCommerceFragment());
        binding.vp2Commerce.setAdapter(commerceViewPagerAdapter);
        new TabLayoutMediator(binding.tlCommerce, binding.vp2Commerce, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Productos");
                        break;
                    case 1:
                        tab.setText("Reseñas");
                        break;
                }
            }
        }).attach();
    }

    private void setValues() {
        mDatabase.child("shops").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    binding.tvCommcerceName.setText(snapshot.child("name").getValue().toString());
                    if (snapshot.child("banner_url").exists()){
                        Glide.with(CommerceActivity.this).load(snapshot.child("banner_url").getValue().toString()).into(binding.ivCommcerceBanner); // Coloca la imagen en el imageview
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}