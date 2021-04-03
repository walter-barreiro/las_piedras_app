package com.example.laspiedrasapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.laspiedrasapp.databinding.FragmentProfileBinding;
import com.example.laspiedrasapp.models.ProfileModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {
    private FirebaseAuth mAuth; // Para poder obtener el id del usuario
    private DatabaseReference mDatabase; // Para extraer los datos de firebase
    private FragmentProfileBinding binding; // Para usar View Binding
    private ProfileModel profileModel;
    StorageReference storageReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // Para el menu
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();// Inicializo el auth del usuario
        mDatabase = FirebaseDatabase.getInstance().getReference(); // Inicializo firebase
        storageReference = FirebaseStorage.getInstance().getReference(); // Para guardar las fotos de perfil en storage

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentProfileBinding.bind(view);// Inicializo el binding
        String userId= mAuth.getCurrentUser().getUid(); // Obtengo el id del usuario logeado
        // Extraigo los datos del usuario de firebase
        String email = mAuth.getCurrentUser().getEmail();

        // Recupero los datos del perfil del usuario y los coloco en los edit text
        mDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    binding.name.setText(snapshot.child(userId).child("name").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Para cargar la imagen de perfil
        storageReference.child("user_prfile_images/"+userId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(getContext()).load(uri).into(binding.imageView); // Coloca la imagen en el imageview
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

//        Picasso.with(getContext()).load(storageReference.child("user_prfile_images/"+userId).getFile())
//                .into(binding.imageView); // Coloca la imagen en el imageview

        // Coloco la imagen de perfil en el imageView
//        Glide.with(this)
//                .load(storageReference.child("user_prfile_images/"+userId))
//                .into(binding.imageView);




//        mDatabase.child("users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
////                    Log.e("firebase", "Error getting data", task.getException());
//                    Toast.makeText(getContext(),"Compruebe internet", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    if(task.getResult().exists()){ // Me fijo si el documento con userId existe (no es null)
//                        profileModel = task.getResult().getValue(ProfileModel.class); // guardo la respuesta en un ProfileModel
//                        binding.name.setText(profileModel.getName());
//                    }else{
//                        binding.name.setText(email);
//                    }
//                }
//            }
//        });
        // ----
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
        // -----
        // Para ir a la tienda
        binding.goBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Ir a mi servicio",Toast.LENGTH_SHORT).show();
            }
        });
        //-----
        // Para ir al servicio
        binding.goCommerce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Ir a mi tienda",Toast.LENGTH_SHORT).show();
            }
        });
        //-----

        binding.fabAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

    }

    private void openDialog() {
        // Abro un dialgofragment para ingresar el nuevo producto
        NewProductFragment newProductFragment1 = new NewProductFragment();
        newProductFragment1.show(getActivity().getSupportFragmentManager(),"newProduct");
        // -----

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null; // Para liberar memoria
    }
}