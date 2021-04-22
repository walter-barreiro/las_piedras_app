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
import com.example.laspiedrasapp.models.ProfileProductModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class BusquedaCategoria extends Fragment{
    FragmentBusquedaCategoriaBinding binding;
    CollectionReference ref;
    Query query;
    CategoriaModel catMod;
    String cate;
    ArrayList<ProfileProductModel> filtro_categoria = new ArrayList<ProfileProductModel>();
    RecyclerView RVSelectCat; // RecyclerView usado con FireBaseAdapter
    AdapterBusquedas adaptadorFireStore;  // adaptador de buscar y filtrar "REALTIME FIREBASE"
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
    //-------------------------------FIRESTORE-----------------------------------
    private void cargaProductosDentroCategorias() {
        FirebaseFirestore reference = FirebaseFirestore.getInstance();
        query = reference.collection("products").orderBy("product_category").whereEqualTo("product_category",cate);
        FirestoreRecyclerOptions<ProfileProductModel> options =
                new FirestoreRecyclerOptions.Builder<ProfileProductModel>()
                        .setQuery(query, ProfileProductModel.class).build();

        adaptadorFireStore = new AdapterBusquedas(options);
        RVSelectCat.setAdapter(adaptadorFireStore);
    }

    @Override
    public void onStart() {
        super.onStart();
        adaptadorFireStore.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adaptadorFireStore.stopListening();
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
        FirestoreRecyclerOptions<ProfileProductModel> options =
                new FirestoreRecyclerOptions.Builder<ProfileProductModel>()
                        .setQuery(FirebaseFirestore.getInstance()
                        .collection("products").orderBy("product_name").whereEqualTo("product_category",cate)
                        .startAt(s).endAt(s+"\uf8ff"), ProfileProductModel.class)
                        .build();

        adaptadorFireStore=new AdapterBusquedas(options);
        adaptadorFireStore.startListening();
        RVSelectCat.setAdapter(adaptadorFireStore);
    }
    //-----------------------------------------------------------------------------------------------------------------------

    private void mostrarListaCategoriasRecibida() {
        ref= FirebaseFirestore.getInstance().collection("products");

        //Recibo datos de StoreFragment por Serializable
        Bundle bundle = this.getArguments();
        catMod = (CategoriaModel) bundle.getSerializable("cat");
        cate = catMod.getTitle();
        Log.i("Mensaje Recibido", "Mi mensaje fue recibido: "+catMod.getTitle());
        ref.orderBy("product_category").equals(catMod.getTitle()); //Filtro que traiga todos los items con la condicion filtrada
        ListarProductos();                 //cargar la lista de los Productos consultados en RealTimeDB
    }

    private void ListarProductos() {
        FirebaseFirestore ref = FirebaseFirestore.getInstance();
        ref.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snap: task.getResult()) {
                                ProfileProductModel cat  = snap.toObject(ProfileProductModel.class);
                                filtro_categoria.add(cat);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
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

}