package com.example.laspiedrasapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laspiedrasapp.R;
import com.example.laspiedrasapp.adapters.MainBusinessAdapter;
import com.example.laspiedrasapp.databinding.FragmentMainBusinessBinding;
import com.example.laspiedrasapp.models.BusinessModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainBusinessFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainBusinessFragment extends Fragment {

    private FirebaseAuth mAuth;
    private CollectionReference mDatabase;
    private FragmentMainBusinessBinding binding;
    private MainBusinessAdapter mainBusinessAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainBusinessFragment() {
        // Required empty public constructor
    }

    public static MainBusinessFragment newInstance(String param1, String param2) {
        MainBusinessFragment fragment = new MainBusinessFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance().collection("business");
        return inflater.inflate(R.layout.fragment_main_business, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentMainBusinessBinding.bind(view);

        initRecyclerView();
        searchViewListeners();
    }

    @Override
    public void onStart() {
        super.onStart();
        mainBusinessAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mainBusinessAdapter.stopListening();
    }

    private void initRecyclerView() {
        binding.rvMainBusiness.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false));
        FirestoreRecyclerOptions<BusinessModel> options = new FirestoreRecyclerOptions.Builder<BusinessModel>()
                .setQuery(mDatabase, BusinessModel.class).build();
        mainBusinessAdapter = new MainBusinessAdapter(options, getContext());
        binding.rvMainBusiness.setAdapter(mainBusinessAdapter);
    }

    private void searchViewListeners() {
        binding.svMainBusiness.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery(query);
                return false;
            }


            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        binding.svMainBusiness.setOnCloseListener(() -> {
           searchQuery("");
            return false;
        });
    }

    private void searchQuery(String s) {
        binding.rvMainBusiness.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        FirestoreRecyclerOptions<BusinessModel> options = new FirestoreRecyclerOptions.Builder<BusinessModel>()
                .setQuery(mDatabase.orderBy("profession").startAt(s).endAt(s+"\uf8ff"), BusinessModel.class).build();
        mainBusinessAdapter = new MainBusinessAdapter(options, getContext());
        mainBusinessAdapter.startListening();
        binding.rvMainBusiness.setAdapter(mainBusinessAdapter);
    }


}