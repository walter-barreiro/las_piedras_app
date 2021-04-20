package com.example.laspiedrasapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.laspiedrasapp.R;
import com.example.laspiedrasapp.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private static final String USERS_COLLECTIONS = "users";
    private static final String USERS_PROFILE_IMAGES_STORAGE = "user_prfile_images";
    private static final String USERS_PRODUCTS_COLLECTIONS = "products";
    private static final String USERS_PRODUCTS_IMAGES_STORAGE = "user_product_images";

    private static final String COMMERCE_COLLECTIONS = "shops";
    private static final String COMMERCE_PRODUCTS_COLLECTIONS = "shops_products";
    private static final String COMMERCE_BANNERS_STORAGE = "shops_banners";
    private static final String COMMERCE_PRODUCTS_IMAGES_STORAGE = "commerce_product_images";


    private static final String BUSINESS_COLLECTIONS = "business";
    private static final String BUSINESS_PROFILE_PHOTO_STORAGE = "business_profile_photo";

    private static final String WHOLESALE_PRODUCTS = "wholesale_products";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Para que funcione la navegacion entre fragments
        BottomNavigationView bottomNavegationView = findViewById(R.id.bnv_main);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavegationView,navController);
        // ----
    }

    public static String getUsersCollections() {
        return USERS_COLLECTIONS;
    }

    public static String getUsersProfileImagesStorage() {
        return USERS_PROFILE_IMAGES_STORAGE;
    }

    public static String getUsersProductsCollections() {
        return USERS_PRODUCTS_COLLECTIONS;
    }

    public static String getUsersProductsImagesStorage() {
        return USERS_PRODUCTS_IMAGES_STORAGE;
    }

    public static String getCommerceCollections() {
        return COMMERCE_COLLECTIONS;
    }

    public static String getCommerceProductsCollections() {
        return COMMERCE_PRODUCTS_COLLECTIONS;
    }

    public static String getCommerceBannersStorage() {
        return COMMERCE_BANNERS_STORAGE;
    }

    public static String getCommerceProductsImagesStorage() {
        return COMMERCE_PRODUCTS_IMAGES_STORAGE;
    }

    public static String getBusinessCollections() {
        return BUSINESS_COLLECTIONS;
    }

    public static String getBusinessProfilePhotoStorage() {
        return BUSINESS_PROFILE_PHOTO_STORAGE;
    }

    public static String getWholesaleProducts() {
        return WHOLESALE_PRODUCTS;
    }
}