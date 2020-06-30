package com.example.mephigrowbox;

import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BottomSheetMust extends BottomSheetDialogFragment {

    private TextView temp_txt, hum_txt, light_txt, plant_txt;

    private FirebaseDatabase cDatabase;
    private DatabaseReference light, humiditySoil, temperature, plant;
    private FirebaseAuth auth;
    FirebaseUser user = auth.getInstance().getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_must, container, false);

        cDatabase = FirebaseDatabase.getInstance();
        light = cDatabase.getReference().child(user.getUid()).child("MustPhotoresistor");
        humiditySoil = cDatabase.getReference().child(user.getUid()).child("MustHumiditySoil");
        temperature = cDatabase.getReference().child(user.getUid()).child("MustTemperature");
        plant = cDatabase.getReference().child(user.getUid()).child("Plant");

        temp_txt = view.findViewById(R.id.must_temp);
        hum_txt = view.findViewById(R.id.must_humidity);
        light_txt = view.findViewById(R.id.must_light);
        plant_txt = view.findViewById(R.id.must_plant);

        plant.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = (String) dataSnapshot.getValue();
                if (value!=null) {
                    String s = "Растение: " + value;
                    plant_txt.setText(s);
                }else{
                    String s = "Растение: НЕ ВЫБРАНО";
                    plant_txt.setText(s);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("ERROR", databaseError.getMessage());
            }
        });

        light.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){
                    long value = (long) dataSnapshot.getValue();
                    String s = "Освещенность: " + value + " ед.";
                    light_txt.setText(s);
                }else{
                    String s = "Освещенность: НЕ ВЫБРАНО";
                    light_txt.setText(s);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("ERROR", databaseError.getMessage());
            }
        });

        humiditySoil.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){
                    long value = (long) dataSnapshot.getValue();
                    String s = "Влажность: " + value + "%";
                    hum_txt.setText(s);
                }else{
                    String s = "Влажность: НЕ ВЫБРАНО";
                    hum_txt.setText(s);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("ERROR", databaseError.getMessage());
            }
        });

        temperature.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){
                    long value = (long) dataSnapshot.getValue();
                    String s = "Температура: " + value + " °C";
                    temp_txt.setText(s);
                }else{
                    String s = "Температура: НЕ ВЫБРАНО";
                    temp_txt.setText(s);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("ERROR", databaseError.getMessage());
            }
        });

        return view;
    }

}
