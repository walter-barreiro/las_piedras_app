package com.example.laspiedrasapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.laspiedrasapp.adapters.ReviewCommerceAdapter;
import com.example.laspiedrasapp.databinding.ActivityBusinessBinding;
import com.example.laspiedrasapp.models.BusinessModel;
import com.example.laspiedrasapp.models.ReviewCommerceModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BusinessActivity extends AppCompatActivity {
    // ToDo hacer el recyclerview, el adaptador, el item_revew_business para mostrar las review
    // ToDo  traer los datos del perfil profesional y dibujarlos en esta activity
    // DONE
    // ToDo modelo de las review:  Voy a usar el mismo modelo que el del commerce
    private final String BUSINESS_COLLECTION = "business";
    private ActivityBusinessBinding binding;
    private ReviewCommerceAdapter reviewCommerceAdapter;
    private List<ReviewCommerceModel> elements = new ArrayList<>(); // Para el recyclervew
    private String userId;
    private FirebaseAuth mAuth; // Para poder obtener el id del usuario
    private DatabaseReference mDatabase; // Para extraer los datos de firebase
    private BusinessModel business = new BusinessModel();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBusinessBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initFirebase();


        // Recupero los datos del profesional, si el usuario toco el boton de Ir a perfil
        String businessId = getIntent().getStringExtra("businessId");
        if(businessId!=null){
            userId = businessId;
            binding.btnEditBusiness.setVisibility(View.GONE);
        }

        initRecyclerView();// Inicializo el recyclerview
        // ToDo recuperar datos de usuario y si no los hay entonces dibujo datos por defecto
        getValuesFromFirebase(); // Obtiene los datos de firebase y los guarda en business
        getReviewsFromDatabase();// Obtengo las review de la base de datos

        binding.btnEditBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Voy a la activity EditBusinessActivity y le paso el objeto con los datos del usuario
                Intent intent = new Intent(BusinessActivity.this,EditBusinessActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("business",business); // le paso los datos por el bundle
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    private void setValues() {
        binding.tvProfesion.setText(business.getProfession());
        Glide.with(BusinessActivity.this).load(business.getImgUrl()).into(binding.ivBusinessImage); // Coloca la imagen en el imageview
    }

    private void getValuesFromFirebase() {
        mDatabase.child(BUSINESS_COLLECTION).child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    business = snapshot.getValue(BusinessModel.class);
                    binding.tvProfesion.setText(business.getProfession());
                    binding.tvLocation.setText(business.getLocation());
                    binding.tvName.setText(business.getName());
                    binding.tvDescription.setText(business.getDescription());

                    if (snapshot.child("imgUrl").exists()){
                        String imgUrl = snapshot.child("imgUrl").getValue().toString();
                        Glide.with(BusinessActivity.this).load(imgUrl).into(binding.ivBusinessImage); // Coloca la imagen en el imageview
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void getReviewsFromDatabase(){
//        reviewCommerceAdapter.notifyDataSetChanged();
        mDatabase.child(BUSINESS_COLLECTION).child(userId).child("reviews").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                elements.clear();
                if(snapshot.exists()){
                    for (DataSnapshot ds: snapshot.getChildren()){ 
                        ReviewCommerceModel reviewCommerceModel = new ReviewCommerceModel();
//                        reviewCommerceModel = snapshot.getValue(ReviewCommerceModel.class);
                        reviewCommerceModel.setReview(ds.child("review").getValue().toString());
                        elements.add(reviewCommerceModel);
                        reviewCommerceAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // ToDo mostrar mensaje de error
                Toast.makeText(BusinessActivity.this, "No hay datos", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void initRecyclerView() {
        reviewCommerceAdapter = new ReviewCommerceAdapter(elements,BusinessActivity.this, new ReviewCommerceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ReviewCommerceModel item) {
                Toast.makeText(BusinessActivity.this, "Nombre "+item.getReview(), Toast.LENGTH_SHORT).show();
            }
        });
        binding.rvBusinessReview.setHasFixedSize(true);
        binding.rvBusinessReview.setLayoutManager(new LinearLayoutManager(BusinessActivity.this));
        binding.rvBusinessReview.setAdapter(reviewCommerceAdapter);
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();// Inicializo el auth del usuario
        mDatabase = FirebaseDatabase.getInstance().getReference(); // Inicializo firebase
        userId= mAuth.getCurrentUser().getUid(); // Obtengo el id del usuario logeado
    }


}