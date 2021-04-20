package com.example.laspiedrasapp.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laspiedrasapp.R;
import com.example.laspiedrasapp.adapters.AdapterBusquedas;
import com.example.laspiedrasapp.adapters.AdapterProductoFiltrado;
import com.example.laspiedrasapp.databinding.FragmentBusquedaCategoriaBinding;
import com.example.laspiedrasapp.models.CategoriaModel;
import com.example.laspiedrasapp.models.ProductoModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BusquedaCategoria extends Fragment{
    FragmentBusquedaCategoriaBinding binding; //Binding (para simplificar)
    DatabaseReference ref; //  Contendrá referencia a la DB de Firebase
    Query mQuery;          //  Aqui se guardan las consultas
    String cate;           //  Contendrá los datos de la categoria Seleccionada (pasados desde StoreFragment)
    ArrayList<ProductoModel> filtro_categoria = new ArrayList<ProductoModel>();  // Lista usada para mostrar Productos filtrados por categoria
    RecyclerView RVSelectCat; // RecyclerView usado con FireBaseAdapter
    AdapterBusquedas adaptadorFireBase;  // adaptador de buscar y filtrar "REALTIME FIREBASE"
    AdapterProductoFiltrado adaptProdFilt; //Adaptador


    public BusquedaCategoria() { }  // Constructor vacio requerido
    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_busqueda_categoria, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentBusquedaCategoriaBinding.bind(view);// Inicializo el binding de "BusquedaCategoria"
        RVSelectCat = (RecyclerView) binding.rvSelectCat;
        RVSelectCat.setLayoutManager(new LinearLayoutManager(getContext()));
        mostrarListaCategoriasRecibida();  //muestra lista de categorias (Datos recibidos desde StoreFragment ("Serializable"))
        cargaProductosDentroCategorias();  //Sublista de Productos filtrados segun su Categoria
        filtrarBusqueda();               //Buscador dentro de Sublista filtrada segun su categoria
    }

    //-------------------------------FIREBASEADAPTER-----------------------------------
    private void cargaProductosDentroCategorias() {
        FirebaseRecyclerOptions<ProductoModel> options =
                new FirebaseRecyclerOptions.Builder<ProductoModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Productos/items").orderByChild("categoria").equalTo(cate), ProductoModel.class)
                        .build();
        adaptadorFireBase = new AdapterBusquedas(options);
        RVSelectCat.setAdapter(adaptadorFireBase);
    }

    @Override
    public void onStart() {
        super.onStart();
        adaptadorFireBase.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adaptadorFireBase.stopListening();
    }

    private void filtrarBusqueda() {
        binding.searchProduct.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
    }
    private void buscar(String s)
    {
        FirebaseRecyclerOptions<ProductoModel> options =
                new FirebaseRecyclerOptions.Builder<ProductoModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference()
                        .child("Productos/items").orderByChild("nombre")
                        .startAt(s).endAt(s+"\uf8ff"), ProductoModel.class)
                        .build();

        adaptadorFireBase=new AdapterBusquedas(options);
        adaptadorFireBase.startListening();
        RVSelectCat.setAdapter(adaptadorFireBase);
    }

    //-----------------------------------------------------------------------------------------------------------------------

    private void mostrarListaCategoriasRecibida() {
        ref= FirebaseDatabase.getInstance().getReference().child("Productos/items");


        //Recibo datos de StoreFragment por Serializable
        Bundle bundle = this.getArguments();
        CategoriaModel catMod = (CategoriaModel) bundle.getSerializable("cat");
        cate = catMod.getTitle();
        Log.i("Mensaje Recibido", "Mi mensaje fue recibido: "+catMod.getTitle());
        mQuery = ref.orderByChild("categoria").equalTo(catMod.getTitle()); //Filtro que traiga todos los items con la condicion filtrada
        ListarProductos();                 //cargar la lista de los Productos consultados en RealTimeDB
    }

    private void ListarProductos() {
        mQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for(DataSnapshot snap : snapshot.getChildren()) {
                        ProductoModel pr = snap.getValue(ProductoModel.class);
                        filtro_categoria.add(pr);
                    }
                    adaptProdFilt.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        integracionConAdaptador();//mostrar data
    }

    @SuppressLint("NewApi")
    private void integracionConAdaptador() {
        RVSelectCat.setLayoutManager(new LinearLayoutManager(getContext()));
        adaptProdFilt = new AdapterProductoFiltrado(getContext(), filtro_categoria);
        RVSelectCat.setAdapter(adaptProdFilt);
    }

    //----------------------------------------------------------------------------------------------------------------------------------

}