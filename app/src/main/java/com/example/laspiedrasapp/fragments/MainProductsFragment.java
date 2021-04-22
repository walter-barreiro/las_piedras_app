package com.example.laspiedrasapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laspiedrasapp.R;
import com.example.laspiedrasapp.adapters.CommerceMainProductAdapter;
import com.example.laspiedrasapp.databinding.FragmentMainProductsBinding;
import com.example.laspiedrasapp.models.CommerceProductModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainProductsFragment extends Fragment {
    FragmentMainProductsBinding binding; //Binding (para simplificar)
    CommerceMainProductAdapter adaptador;
    RecyclerView RV; // RecyclerView usado con FireBaseAdapter

    public MainProductsFragment() { } // Required empty public constructor
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_products, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentMainProductsBinding.bind(view);// Inicializo el binding

        RV = (RecyclerView) binding.rvMainProdFrag;
        RV.setLayoutManager(new GridLayoutManager(getContext(),3));

        listarProductosCommerce();
        filtrarBusqueda();
    }

    private void listarProductosCommerce() {
        FirestoreRecyclerOptions<CommerceProductModel> options =
                new FirestoreRecyclerOptions.Builder<CommerceProductModel>()
                        .setQuery(FirebaseFirestore.getInstance().collection("shops_product"), CommerceProductModel.class)
                        .build();
        adaptador = new CommerceMainProductAdapter(options);
        adaptador.startListening();
        RV.setAdapter(adaptador);
    }

    @Override
    public void onStart() {
        super.onStart();
        adaptador.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adaptador.stopListening();
    }

    private void filtrarBusqueda() {
        binding.searchProductFrag.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                buscar(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                //buscar(s);
                return false;
            }
        });

        binding.searchProductFrag.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                listarProductosCommerce();
                return false;
            }
        });
    }
    private void buscar(String s)
    {
        FirestoreRecyclerOptions<CommerceProductModel> options =
                new FirestoreRecyclerOptions.Builder<CommerceProductModel>()
                        .setQuery(FirebaseFirestore.getInstance().collection("shops_product").orderBy("name").startAt(s).endAt(s+"\uf8ff"), CommerceProductModel.class)
                        .build();

        adaptador=new CommerceMainProductAdapter(options);
        adaptador.startListening();
        RV.setAdapter(adaptador);
    }








}