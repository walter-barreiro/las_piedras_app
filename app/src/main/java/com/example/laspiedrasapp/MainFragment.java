package com.example.laspiedrasapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laspiedrasapp.databinding.FragmentMainBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    private FragmentMainBinding binding;
    private MainViewPagerAdapter mainViewPagerAdapter;


    public MainFragment() {
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
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentMainBinding.bind(view);
        initTabLayoutViewPager2();
        binding.vp2MainFragment.setUserInputEnabled(false); // Para desactivar el swiping

        binding.irCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShoppingCartActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initTabLayoutViewPager2() {
        mainViewPagerAdapter = new MainViewPagerAdapter(getActivity().getSupportFragmentManager(),getLifecycle());
        mainViewPagerAdapter.addFragment(new MainProductsFragment());
        mainViewPagerAdapter.addFragment(new MainCommerceFragment());
        mainViewPagerAdapter.addFragment(new MainBusinessFragment());
        binding.vp2MainFragment.setAdapter(mainViewPagerAdapter);
        new TabLayoutMediator(binding.tlMainFragment, binding.vp2MainFragment, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Productos");
                        break;
                    case 1:
                        tab.setText("Comercios");
                        break;
                    case 2:
                        tab.setText("Servicios");
                        break;
                }
            }
        }).attach();
    }
}