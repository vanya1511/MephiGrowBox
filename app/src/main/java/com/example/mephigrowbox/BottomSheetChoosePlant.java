package com.example.mephigrowbox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BottomSheetChoosePlant extends BottomSheetDialogFragment {

    private Button nonameImg;
    private Button nonameTxt;
    private Button cucumberImg;
    private Button cucumberTxt;

    private FirebaseDatabase cDatabase;
    private DatabaseReference cReference;
    private FirebaseAuth auth;
    FirebaseUser user = auth.getInstance().getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_choose_plant, container, false);

        cDatabase = FirebaseDatabase.getInstance();
        cReference = cDatabase.getReference().child(user.getUid());

        nonameImg = view.findViewById(R.id.nonamecvetImg);
        nonameTxt = view.findViewById(R.id.nonamecvetTxt);
        cucumberImg = view.findViewById(R.id.cucumberImg);
        cucumberTxt = view.findViewById(R.id.cucumberTxt);

        nonameImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cReference.child("Plant").setValue("Ноунеймцвет");
                cReference.child("MustPhotoresistor").setValue(200);
                cReference.child("MustHumiditySoil").setValue(10);
                cReference.child("MustTemperature").setValue(25);
            }
        });

        nonameTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cReference.child("Plant").setValue("Ноунеймцвет");
                cReference.child("MustPhotoresistor").setValue(200);
                cReference.child("MustHumiditySoil").setValue(10);
                cReference.child("MustTemperature").setValue(25);
            }
        });

        cucumberImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cReference.child("Plant").setValue("Огурец");
                cReference.child("MustPhotoresistor").setValue(320);
                cReference.child("MustHumiditySoil").setValue(70);
                cReference.child("MustTemperature").setValue(30);
            }
        });

        cucumberTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cReference.child("Plant").setValue("Огурец");
                cReference.child("MustPhotoresistor").setValue(320);
                cReference.child("MustHumiditySoil").setValue(70);
                cReference.child("MustTemperature").setValue(30);
            }
        });

        return view;
    }

}
