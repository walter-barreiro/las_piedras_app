package com.example.laspiedrasapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.laspiedrasapp.databinding.FragmentProfileBinding;
import com.example.laspiedrasapp.models.ProfileModel;
import com.example.laspiedrasapp.models.ProfileProductModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    private FirebaseAuth mAuth; // Para poder obtener el id del usuario
    private DatabaseReference mDatabase; // Para extraer los datos de firebase
    private StorageReference storageReference;
    private FragmentProfileBinding binding; // Para usar View Binding
    private ProfileModel profileModel;
    private String userId;
    private String email;
    private Uri product_image;
    List<ProfileProductModel> elements = new ArrayList<>(); // Para el recyclervew
    private ProfileProductAdapter profileProductAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // Para el menu
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initFirebase(); // Inicializo lo de firebase
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentProfileBinding.bind(view);// Inicializo el binding
        // Recupero los datos del perfil del usuario y los coloco en los edit text
        mDatabase.child("users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    binding.name.setText(snapshot.child("name").getValue().toString());

                    if (snapshot.child("verified").exists()){
                        if ( snapshot.child("verified").getValue().toString() == "true" ){
                            binding.name.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_verified,0);
                        }
                    }
                    Glide.with(getContext()).load(snapshot.child("imgUrl").getValue().toString()).into(binding.imageView); // Coloca la imagen en el imageview
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        initRecyclerView(); // Inicializo el recycler view
        getProductsFromDatabase();// Obtengo los productos del usuario
        // Para el toolbar
        binding.toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()){
                case R.id.item_add_commerce:
                    Toast.makeText(getActivity(), "Agregr comercio",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent( getActivity(), CommerceActivity.class)); // Para ir al registro
                    return true;
                case R.id.item_add_service:
                    Toast.makeText(getActivity(), "Agregar servicio",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent( getActivity(), BusinessActivity.class)); // Para ir al registro
                    return true;
                case R.id.item_conf_prof:
                    startActivity(new Intent( getActivity(), EditProfileActivity.class)); // Para ir al registro
                    return true;
                case R.id.item_contact:
                    Toast.makeText(getActivity(), "Contacto",Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.item_about:
                    Toast.makeText(getActivity(), "Acerca de",Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.item_logout:
                    FirebaseAuth.getInstance().signOut(); // Para cerrar la sesion
                    startActivity(new Intent( getActivity(), RegisterActivity.class)); // Para ir al registro
                    getActivity().finish(); // Para cerrar el Activity Main
                    return true;
                default: return super.onOptionsItemSelected(item);

            }
        });
        // Para ir al servicio
        binding.goBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Ir a mi servicio",Toast.LENGTH_SHORT).show();
            }
        });
        // Para ir a la tienda
        binding.goCommerce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Ir a mi tienda",Toast.LENGTH_SHORT).show();
            }
        });
        // Para agergar nuevo producto
        binding.fabAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null; // Para liberar memoria
    }

    private void openDialog() {
        // Abro un dialgofragment para ingresar el nuevo producto
        NewProductFragment newProductFragment1 = new NewProductFragment();
        newProductFragment1.show(getActivity().getSupportFragmentManager(),"newProduct");
        // -----

    }

    private void initRecyclerView() {
        // Para cuando el usuario presione un item
        profileProductAdapter = new ProfileProductAdapter(elements, getContext(), new ProfileProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ProfileProductModel item) {
//                Toast.makeText(getContext(), "Nombre "+item.getProduct_name(), Toast.LENGTH_SHORT).show();
                EditProductProfileFragment editProductProfileFragment = new EditProductProfileFragment();
                Bundle bundle= new Bundle();
                bundle.putSerializable("product",item);
                editProductProfileFragment.setArguments(bundle);
                editProductProfileFragment.show(getActivity().getSupportFragmentManager(),"editProduct");

            }
        });
        binding.rvProfileProduct.setHasFixedSize(true);
//        binding.rvProfileProduct.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvProfileProduct.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.rvProfileProduct.setAdapter(profileProductAdapter);

    }

    private void getProductsFromDatabase(){
        // Voy a los productos que tiene el usuario en la base  de  datos
        mDatabase.child("users").child(userId).child("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                elements.clear();
                if(snapshot.exists()){
                    for (DataSnapshot ds: snapshot.getChildren()){ // ds tiene el children de products. Recorro  todos los productos y obtengo el id
                        String productId = ds.getKey().toString(); // obtengo los id de los productos del cliente
                        mDatabase.child("products").child(productId).addValueEventListener(new ValueEventListener() { // para  cada id recorro los productos para obtener los datos
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    ProfileProductModel profileProductModel = new ProfileProductModel();
                                    profileProductModel.setProduct_name(snapshot.child("product_name").getValue().toString());
                                    profileProductModel.setProduct_price(snapshot.child("product_price").getValue().toString());
                                    profileProductModel.setProduct_image_url(snapshot.child("product_image_url").getValue().toString());
                                    profileProductModel.setProduct_id(productId);
                                    elements.add(profileProductModel);//:snapshot.child("product_name").getValue().toString())); // agrego eloproductId a la lista de elementos
//                                    initRecyclerView();
                                    profileProductAdapter.notifyDataSetChanged();
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

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();// Inicializo el auth del usuario
        mDatabase = FirebaseDatabase.getInstance().getReference(); // Inicializo firebase
        storageReference = FirebaseStorage.getInstance().getReference(); // Para guardar las fotos de perfil en storage
        userId= mAuth.getCurrentUser().getUid(); // Obtengo el id del usuario logeado
        email = mAuth.getCurrentUser().getEmail();// Extraigo los datos del usuario de firebase
    }


}