package com.example.laspiedrasapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laspiedrasapp.R;
import com.example.laspiedrasapp.adapters.MainCommerceAdapter;
import com.example.laspiedrasapp.databinding.FragmentMainCommerceBinding;
import com.example.laspiedrasapp.models.CommerceModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainCommerceFragment extends Fragment {
    private FirebaseAuth mAuth; // Para poder obtener el id del usuario
    private CollectionReference mDatabase; // Para extraer los datos de firebase
    private FragmentMainCommerceBinding binding;
    private MainCommerceAdapter mainCommerceAdapter;


    public MainCommerceFragment() {
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
        mDatabase = FirebaseFirestore.getInstance().collection("shops"); // Inicializo firebase
        return inflater.inflate(R.layout.fragment_main_commerce, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentMainCommerceBinding.bind(view);

        initRecyclerView();
        searchViewListeners();
    }

    @Override
    public void onStart() {
        super.onStart();
        mainCommerceAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mainCommerceAdapter.stopListening();
    }

    private void initRecyclerView(){
        binding.rvMainCommerce.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false));
        FirestoreRecyclerOptions<CommerceModel> options = new FirestoreRecyclerOptions.Builder<CommerceModel>()
                .setQuery(mDatabase, CommerceModel.class).build();
        mainCommerceAdapter = new MainCommerceAdapter(options,getContext());
        binding.rvMainCommerce.setAdapter(mainCommerceAdapter);
    }
    private void searchViewListeners() {
        binding.svMainCommerce.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        binding.svMainCommerce.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchQuery("");
                return false;
            }
        });

    }
    private void searchQuery(String s) {
        binding.rvMainCommerce.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false));
        FirestoreRecyclerOptions<CommerceModel> options = new FirestoreRecyclerOptions.Builder<CommerceModel>()
                .setQuery(mDatabase.orderBy("name").startAt(s).endAt(s+"\uf8ff"), CommerceModel.class).build();
        mainCommerceAdapter = new MainCommerceAdapter(options,getContext());
        mainCommerceAdapter.startListening();
        binding.rvMainCommerce.setAdapter(mainCommerceAdapter);
    }
}