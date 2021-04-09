package com.example.laspiedrasapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.laspiedrasapp.databinding.FragmentEditProductProfileBinding;
import com.example.laspiedrasapp.databinding.FragmentProfileBinding;
import com.example.laspiedrasapp.models.ProfileProductModel;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import static android.app.Activity.RESULT_OK;

public class EditProductProfileFragment extends DialogFragment {
    private FragmentEditProductProfileBinding binding;
    private ProfileProductModel profileProductModel;
    private Uri resultUri;

    public EditProductProfileFragment() {
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
        View root = inflater.inflate(R.layout.fragment_edit_product_profile, container, false);
        // Recupero el producto
        Bundle bundle = this.getArguments();
        profileProductModel = (ProfileProductModel) bundle.getSerializable("product");
        // ---


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentEditProductProfileBinding.bind(view);// Inicializo el binding

        binding.tvEditProductName.setText(profileProductModel.getProduct_name());
        binding.tvEditProductPrice.setText(profileProductModel.getProduct_price());
//        Picasso.with(getContext()).load(profileProductModel.getProduct_image_url()).into(binding.ivEdit);
        Glide.with(getContext()).load(profileProductModel.getProduct_image_url()).into(binding.ivEdit);

        binding.btnEditCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.btnEditSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.tvEditProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setRequestedSize(640,480)
                        .setAspectRatio(1,1).start(getContext(), EditProductProfileFragment.this);
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                binding.ivEdit.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}