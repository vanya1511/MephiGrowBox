package com.example.mephigrowbox;

import android.os.Bundle;
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

public class FragmentWater extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference humidityAir;
    private TextView humidityTxt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_water, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        database = FirebaseDatabase.getInstance();
        humidityTxt = getActivity().findViewById(R.id.humidity_txt);
        humidityAir = database.getReference("HumidityAir");

        humidityAir.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long value = (long) dataSnapshot.getValue();
                String s = "Влажность воздуха\n" + value + "%";
                humidityTxt.setText(s);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
