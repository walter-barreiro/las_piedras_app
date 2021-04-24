package com.example.laspiedrasapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.laspiedrasapp.adapters.CommerceViewPagerAdapter;
import com.example.laspiedrasapp.databinding.ActivityCommerceBinding;
import com.example.laspiedrasapp.fragments.ProductsCommerceFragment;
import com.example.laspiedrasapp.fragments.ReviewsCommerceFragment;
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

        // Todo Recivir el ownerId

        String ownerId = getIntent().getStringExtra("ownerId");
        if(ownerId!=null){
            userId = ownerId;
            binding.btnCommerceEdit.setVisibility(View.GONE);
        }


        binding.vp2Commerce.setUserInputEnabled(false);
        setValues();// Obtengo los datos de firebase y los coloco en los view
        initTabLayoutViewPager2();// Para la navegacion entre Productos y reseñas

        binding.btnCommerceEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommerceActivity.this,EditCommerceActivity.class);
                startActivity(intent); // Para ir al registro
            }
        });


        binding.btnCommerceLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final String[] latitude = new String[1];
//                final String[] longitude = new String[1];
//                mDatabase.child("shops").child(ownerId).child("coordinates").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        latitude[0] = snapshot.child("latitud").getValue().toString();
//                        longitude[0] = snapshot.child("longitud").getValue().toString();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//                Uri gmmIntentUri = Uri.parse("google.navigation:q="+latitude[0]+","+longitude[0]);
//                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                mapIntent.setPackage("com.google.android.apps.maps");
//                startActivity(mapIntent);
            }
        });

    }

    private void initTabLayoutViewPager2() {
        commerceViewPagerAdapter = new CommerceViewPagerAdapter(getSupportFragmentManager(),getLifecycle());
        // Creo el fragmento y le paso el id
        ProductsCommerceFragment productsCommerceFragment = new ProductsCommerceFragment();
        Bundle bundle = new Bundle();
        bundle.putString("ownerId",userId);
        productsCommerceFragment.setArguments(bundle);
        commerceViewPagerAdapter.addFragment(productsCommerceFragment);
        //----
        ReviewsCommerceFragment reviewsCommerceFragment =new ReviewsCommerceFragment();
        reviewsCommerceFragment.setArguments(bundle);
        commerceViewPagerAdapter.addFragment(reviewsCommerceFragment);
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