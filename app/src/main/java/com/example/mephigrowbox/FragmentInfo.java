package com.example.mephigrowbox;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class FragmentInfo extends Fragment {

    private BottomSheetDialogFragment bottom_sheet_choose_plant = null;
    private BottomSheetDialogFragment bottom_sheet_must = null;

    private TextView temp_txt, hum_txt, light_txt, plant_txt;
    private Button chooseBtn, helpBtn;
    FirebaseDatabase database;
    private FirebaseAuth auth;
    FirebaseUser user = auth.getInstance().getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        temp_txt = getActivity().findViewById(R.id.info_temp);
        hum_txt = getActivity().findViewById(R.id.info_hum);
        light_txt = getActivity().findViewById(R.id.info_light);
        plant_txt = getActivity().findViewById(R.id.info_plant);
        chooseBtn =getActivity().findViewById(R.id.choose_button);
        helpBtn = getActivity().findViewById(R.id.help_button);


        database = FirebaseDatabase.getInstance();
        DatabaseReference light = database.getReference().child(user.getUid()).child("Photoresistor");
        DatabaseReference humiditySoil = database.getReference().child(user.getUid()).child("HumiditySoil");
        DatabaseReference temperature = database.getReference().child(user.getUid()).child("Temperature");
        DatabaseReference plant = database.getReference().child(user.getUid()).child("Plant");

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
                long value = (long) dataSnapshot.getValue();
                String s = "Освещенность: " + value + " ед.";
                light_txt.setText(s);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("ERROR", databaseError.getMessage());
            }
        });

        humiditySoil.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long value = (long) dataSnapshot.getValue();
                String s = "Влажность: " + value + "%";
                hum_txt.setText(s);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("ERROR", databaseError.getMessage());
            }
        });

        temperature.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long value = (long) dataSnapshot.getValue();
                String s = "Температура: " + value + "°C";
                temp_txt.setText(s);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("ERROR", databaseError.getMessage());
            }
        });

        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottom_sheet_choose_plant = new BottomSheetChoosePlant();
                bottom_sheet_choose_plant.show(getChildFragmentManager(), "BottomSheetChoosePlant");
            }
        });

        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottom_sheet_must = new BottomSheetMust();
                bottom_sheet_must.show(getChildFragmentManager(), "BottomSheetMust");
            }
        });


    }
}
