package com.example.laspiedrasapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.laspiedrasapp.R;
import com.example.laspiedrasapp.adapters.WholesaleProductAdapter;
import com.example.laspiedrasapp.databinding.FragmentWholesaleBinding;
import com.example.laspiedrasapp.models.WholesaleProductModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class WholesaleFragment extends Fragment {
    // ToDo agregar un onClick a cada elemento y que se abra un DialogFragment para elegir cantidad y agregar al carrito
    private FragmentWholesaleBinding binding;
    private final String WHOLESALE_PRODUCTS_COLLECTION = "wholesale_products";
    private final String WHOLESALE_PRODUCTS_IMAGES_STORAGE = "wholesale_products_images";
    private DatabaseReference mDatabase; // Para extraer los datos de firebase
    private WholesaleProductAdapter wholesaleProductAdapter;
    private List<WholesaleProductModel> elements = new ArrayList<>();
    private WholesaleProductModel wholesaleProductModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mDatabase = FirebaseDatabase.getInstance().getReference(); // Inicializo firebase
        return inflater.inflate(R.layout.fragment_wholesale, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentWholesaleBinding.bind(view);
        initRecyclerView();

        mDatabase.child(WHOLESALE_PRODUCTS_COLLECTION).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            elements.clear();
            if(snapshot.exists()){
                for (DataSnapshot ds: snapshot.getChildren()){
                    if (ds.exists()){
                        wholesaleProductModel = new WholesaleProductModel();
                        wholesaleProductModel = ds.getValue(WholesaleProductModel.class);
                        elements.add(wholesaleProductModel);
                        wholesaleProductAdapter.notifyDataSetChanged();
                    }
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });


    }

    private void initRecyclerView() {
        wholesaleProductAdapter = new WholesaleProductAdapter(elements, getContext(), new WholesaleProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(WholesaleProductModel item) {
                Toast.makeText(getContext(), "Nombre "+item.getTitle(), Toast.LENGTH_SHORT).show();
//                EditProductProfileFragment editProductProfileFragment = new EditProductProfileFragment();
//                Bundle bundle= new Bundle();
//                bundle.putSerializable("product",item);
//                editProductProfileFragment.setArguments(bundle);
//                editProductProfileFragment.show(getActivity().getSupportFragmentManager(),"editProduct");

            }
        });
        binding.rvWholesale.setHasFixedSize(true);
        binding.rvWholesale.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false));
//        binding.rvWholesale.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.rvWholesale.setAdapter(wholesaleProductAdapter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}