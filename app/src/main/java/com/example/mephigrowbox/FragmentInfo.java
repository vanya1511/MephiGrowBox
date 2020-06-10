package com.example.mephigrowbox;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentInfo extends Fragment {

    private TextView temp_txt, hum_txt, light_txt;
    FirebaseDatabase database;

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

        database = FirebaseDatabase.getInstance();
        DatabaseReference light = database.getReference("Photoresistor");
        DatabaseReference humiditySoil = database.getReference("HumiditySoil");
        DatabaseReference temperature = database.getReference("Temperature");

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
                String s = "Влажность: " + value + " ед.";
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
    }
}
