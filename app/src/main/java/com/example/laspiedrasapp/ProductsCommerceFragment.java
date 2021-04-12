package com.example.laspiedrasapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.laspiedrasapp.databinding.FragmentProductsCommerceBinding;
import com.example.laspiedrasapp.databinding.FragmentProfileBinding;
import com.example.laspiedrasapp.models.CommerceProductModel;
import com.example.laspiedrasapp.models.ProfileProductModel;

import java.util.ArrayList;
import java.util.List;

public class ProductsCommerceFragment extends Fragment {
    private FragmentProductsCommerceBinding binding;
    private List<CommerceProductModel> elements = new ArrayList<>(); // Para el recyclervew
    private CommerceProductAdapter commerceProductAdapter;



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
        return inflater.inflate(R.layout.fragment_products_commerce, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentProductsCommerceBinding.bind(view);// Inicializo el binding


//        commerceProductAdapter = new CommerceProductAdapter(elements, getContext(), new CommerceProductAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(CommerceProductModel item) {
////                Toast.makeText(getContext(), "Nombre "+item.getProduct_name(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(getContext(),"Elemento presionado", Toast.LENGTH_SHORT).show();
////                EditProductProfileFragment editProductProfileFragment = new EditProductProfileFragment();
////                Bundle bundle= new Bundle();
////                bundle.putSerializable("product",item);
////                editProductProfileFragment.setArguments(bundle);
////                editProductProfileFragment.show(getActivity().getSupportFragmentManager(),"editProduct");
//
//            }
//        });
//        binding.rvCommerceProduct.setHasFixedSize(true);
////        binding.rvProfileProduct.setLayoutManager(new LinearLayoutManager(getContext()));
//        binding.rvCommerceProduct.setLayoutManager(new GridLayoutManager(getContext(),2));
//        binding.rvCommerceProduct.setAdapter(commerceProductAdapter);


        binding.fabNewProductCommerce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

    }

    private void openDialog() {
        // Abro un dialgofragment para ingresar el nuevo producto
        NewProductCommerceFragment newProductCommerceFragment = new NewProductCommerceFragment();
        newProductCommerceFragment.show(getActivity().getSupportFragmentManager(),"newProductCommerce");
        // -----

    }
}