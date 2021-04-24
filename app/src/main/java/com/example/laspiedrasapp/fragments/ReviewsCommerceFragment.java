package com.example.laspiedrasapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.laspiedrasapp.R;
import com.example.laspiedrasapp.adapters.ReviewCommerceAdapter;
import com.example.laspiedrasapp.databinding.FragmentReviewsCommerceBinding;
import com.example.laspiedrasapp.models.ReviewCommerceModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.internal.operators.observable.ObservableTimeInterval;

public class ReviewsCommerceFragment extends Fragment {
    private FirebaseAuth mAuth; // Para poder obtener el id del usuario
    private DatabaseReference mDatabase; // Para extraer los datos de firebase

    FragmentReviewsCommerceBinding binding;
    private ReviewCommerceAdapter reviewCommerceAdapter;
    List<ReviewCommerceModel> elements = new ArrayList<>(); // Para el recyclervew
    private String userId;

    public ReviewsCommerceFragment() {
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
        initFirebase();
        return inflater.inflate(R.layout.fragment_reviews_commerce, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentReviewsCommerceBinding.bind(view);
        // TODO Colocar un boton para agregar reviews que solo sea visible para personas que visiten el perfil del comerciante
//        binding.btnAddReviewCommerce.setVisibility(View.VISIBLE);

        Bundle bundle = this.getArguments();
        String ownerId = bundle.getString("ownerId");
        if(!ownerId.isEmpty()){
            if(userId!=ownerId){
                binding.btnAddReviewCommerce.setVisibility(View.VISIBLE);
                userId = ownerId;
            }
        }

        binding.btnAddReviewCommerce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.etCommerceReviewWrite.setVisibility(View.VISIBLE);
                binding.btnCommerceReviewCancel.setVisibility(View.VISIBLE);
                binding.btnCommerceReviewSave.setVisibility(View.VISIBLE);
            }
        });

        binding.btnCommerceReviewSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.etCommerceReviewWrite.setVisibility(View.GONE);
                binding.btnCommerceReviewCancel.setVisibility(View.GONE);
                binding.btnCommerceReviewSave.setVisibility(View.GONE);

                String review = binding.etCommerceReviewWrite.getText().toString();
                mDatabase.child("shops").child(ownerId).child("reviews").child(mAuth.getCurrentUser().getUid()).child("review").setValue(review);
            }
        });

        binding.btnCommerceReviewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.etCommerceReviewWrite.setVisibility(View.GONE);
                binding.btnCommerceReviewCancel.setVisibility(View.GONE);
                binding.btnCommerceReviewSave.setVisibility(View.GONE);
            }
        });



        initRecyclerView();
        getReviewsFromDatabase();
    }

    private void initRecyclerView() {
        reviewCommerceAdapter = new ReviewCommerceAdapter(elements, getContext(), new ReviewCommerceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ReviewCommerceModel item) {
                Toast.makeText(getContext(), "Nombre "+item.getReview(), Toast.LENGTH_SHORT).show();
            }
        });
        binding.rvCommerceReviews.setHasFixedSize(true);
        binding.rvCommerceReviews.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvCommerceReviews.setAdapter(reviewCommerceAdapter);
    }

    private void getReviewsFromDatabase(){

        mDatabase.child("shops").child(userId).child("reviews").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                elements.clear();
                if(snapshot.exists()){
                    for (DataSnapshot ds: snapshot.getChildren()){
                        ReviewCommerceModel reviewCommerceModel = new ReviewCommerceModel();
                        reviewCommerceModel.setReview(ds.child("review").getValue().toString());
                        elements.add(reviewCommerceModel);
                        reviewCommerceAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // ToDo mostrar mensaje de error

            }
        });

    }
    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();// Inicializo el auth del usuario
        mDatabase = FirebaseDatabase.getInstance().getReference(); // Inicializo firebase
        userId= mAuth.getCurrentUser().getUid(); // Obtengo el id del usuario logeado
    }
}