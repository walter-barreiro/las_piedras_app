package com.example.laspiedrasapp;

import android.content.Intent;
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

import com.example.laspiedrasapp.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment {
    private FirebaseAuth mAuth; // Para poder obtener el id del usuario
    private DatabaseReference mDatabase; // Para extraer los datos de firebase
    private FragmentProfileBinding binding; // Para usar View Binding

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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentProfileBinding.bind(view);// Inicializo el binding
        String userId= mAuth.getCurrentUser().getUid(); // Obtengo el id del usuario logeado
        // Extraigo los datos del usuario de firebase
        String email = mAuth.getCurrentUser().getEmail();



        // ----
        // Para colocar los datosp del usuario en la vista
        binding.name.setText(email);
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

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null; // Para liberar memoria
    }
}