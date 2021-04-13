package com.example.laspiedrasapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.laspiedrasapp.databinding.FragmentProductsCommerceBinding;
import com.example.laspiedrasapp.databinding.FragmentProfileBinding;
import com.example.laspiedrasapp.models.CommerceProductModel;
import com.example.laspiedrasapp.models.ProfileProductModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductsCommerceFragment extends Fragment {
    // ToDo Hacer un dialog fragment para editar los productos

    private final String PRODUCT_COLLECTION = "shops_product"; // Nombre de la coleccion que tiene los productos de los comercios
    private final String COMMERCE_COLLECTION = "shops"; // Nombre de la coleccion que tiene los datos de los comercios

    private FirebaseAuth mAuth; // Para poder obtener el id del usuario
    private DatabaseReference mDatabase; // Para extraer los datos de firebase

    private FragmentProductsCommerceBinding binding;
    private List<CommerceProductModel> elements = new ArrayList<>(); // Para el recyclervew
    private CommerceProductAdapter commerceProductAdapter;

    private String userId;


    public ProductsCommerceFragment() {
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
        mDatabase = FirebaseDatabase.getInstance().getReference(); // Inicializo firebase
        return inflater.inflate(R.layout.fragment_products_commerce, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentProductsCommerceBinding.bind(view);// Inicializo el binding
        userId= mAuth.getCurrentUser().getUid(); // Obtengo el id del usuario logeado

        initRecyclerView();
        getProductsFromDatabase();


        binding.fabNewProductCommerce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

    }

    private void getProductsFromDatabase() {
        mDatabase.child(COMMERCE_COLLECTION).child(userId).child("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                elements.clear();
                if(snapshot.exists()){
                    for (DataSnapshot ds: snapshot.getChildren()){ // ds tiene el children de products. Recorro  todos los productos y obtengo el id
                        String productId = ds.getKey().toString(); // obtengo los id de los productos del cliente
                        mDatabase.child(PRODUCT_COLLECTION).child(productId).addValueEventListener(new ValueEventListener() { // para  cada id recorro los productos para obtener los datos
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    CommerceProductModel commerceProductModel = snapshot.getValue(CommerceProductModel.class);
                                    elements.add(commerceProductModel);
                                    commerceProductAdapter.notifyDataSetChanged();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initRecyclerView() {
        commerceProductAdapter = new CommerceProductAdapter(elements, getContext(), new CommerceProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CommerceProductModel item) {
//                Toast.makeText(getContext(), "Nombre "+item.getName(), Toast.LENGTH_SHORT).show();
                EditProductCommerceFragment editProductCommerceFragment = new EditProductCommerceFragment();
                Bundle bundle= new Bundle();
                bundle.putSerializable("product_commerce",item);
                editProductCommerceFragment.setArguments(bundle);
                editProductCommerceFragment.show(getActivity().getSupportFragmentManager(),"editProductCommerce");

            }
        });
        binding.rvCommerceProduct.setHasFixedSize(true);
//        binding.rvCommerceProduct.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        binding.rvCommerceProduct.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.rvCommerceProduct.setAdapter(commerceProductAdapter);
    }

    
    private void openDialog() {
        // Abro un dialgofragment para ingresar el nuevo producto
        NewProductCommerceFragment newProductCommerceFragment = new NewProductCommerceFragment();
        newProductCommerceFragment.show(getActivity().getSupportFragmentManager(),"newProductCommerce");
        // -----

    }
}