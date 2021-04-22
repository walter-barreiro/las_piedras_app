package com.example.laspiedrasapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laspiedrasapp.R;
import com.example.laspiedrasapp.adapters.AdapterBusquedas;
import com.example.laspiedrasapp.adapters.AdapterCategoria;
import com.example.laspiedrasapp.databinding.FragmentStoreBinding;
import com.example.laspiedrasapp.models.CategoriaModel;
import com.example.laspiedrasapp.models.ProfileProductModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class StoreFragment extends Fragment {
    RecyclerView recview; //de Firebase
    FragmentStoreBinding binding;
    //    DatabaseReference ref; // Para extraer los datos de firebase  (Categorias)
    List<CategoriaModel> elements = new ArrayList<>(); //
    AdapterBusquedas adapter;  // adaptador de buscar y filtrar
    AdapterCategoria adaptCat;
    FragmentTransaction ft;
    BusquedaCategoria cat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentStoreBinding.bind(view);// Inicializo el binding
        recview = (RecyclerView) binding.rvBuscMainTodo;
        recview.setLayoutManager(new LinearLayoutManager(getContext()));

        iniciarSwich();
        cargaProductos();
        getCategorysFromDatabase();
        initRecyclerView();
        filtrarBusqueda();
    }

    private void cargaProductos() {
        FirestoreRecyclerOptions<ProfileProductModel> options =
                new FirestoreRecyclerOptions.Builder<ProfileProductModel>()
                        .setQuery(FirebaseFirestore.getInstance().collection("products"), ProfileProductModel.class)
                        .build();
        adapter = new AdapterBusquedas(options);
        recview.setAdapter(adapter);
    }

    private void iniciarSwich() {
        binding.switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.switch1.isChecked()) {
                    binding.rv.setVisibility(View.VISIBLE);
                    binding.rvBuscMainTodo.setVisibility(View.GONE);
                    Log.i("Mensaje Pasado", "Esta Activado ");
                } else if (!binding.switch1.isChecked()) {
                    binding.rv.setVisibility(View.GONE);
                    binding.rvBuscMainTodo.setVisibility(View.VISIBLE);
                    Log.i("Mensaje Pasado", "No Esta Activado");
                }
            }
        });
        binding.rv.setVisibility(View.GONE);
    }
    private void getCategorysFromDatabase() {
        FirebaseFirestore ref = FirebaseFirestore.getInstance();
        ref.collection("categoria")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snap: task.getResult()) {
                                //Log.d(TAG, doc.getId() + " => " + doc.getData());
                                CategoriaModel cat  = snap.toObject(CategoriaModel.class);
                                //CategoriaModel cat = (CategoriaModel) snap.getData();
                                elements.add(cat);
                            }
                            adaptCat.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });
    }
//
        private void initRecyclerView() {
        // Para cuando el usuario presione un item
        adaptCat = new AdapterCategoria(elements, getContext(), new AdapterCategoria.OnItemClickListener() {
            @Override
            public void onItemClick(CategoriaModel item) {
                if (item != null) {
                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                    //ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    cat = new BusquedaCategoria();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("cat", item);
                    cat.setArguments(bundle);
                    ft.replace(R.id.busTodo, cat);
                    ft.addToBackStack(null);
                    ft.commit();
                    Log.i("Mensaje Pasado", "Mi mensaje pasado " + item.getTitle());
                }
            }
        });
        binding.rv.setHasFixedSize(true);
        binding.rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.rv.setAdapter(adaptCat);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void filtrarBusqueda() {
        binding.search.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
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
                        .setQuery(FirebaseFirestore.getInstance().collection("products").orderBy("product_name").startAt(s).endAt(s+"\uf8ff"), ProfileProductModel.class)
                        .build();

        adapter=new AdapterBusquedas(options);
        adapter.startListening();
        recview.setAdapter(adapter);
    }

}

