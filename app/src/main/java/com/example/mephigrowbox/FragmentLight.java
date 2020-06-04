package com.example.mephigrowbox;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentLight extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference led, redColor, greenColor, blueColor, light;
    private ImageView lampImg;
    private boolean isTurned = false;
    private SeekBar rBar, gBar, bBar;
    private TextView lightTxt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_light, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        lampImg = getActivity().findViewById(R.id.lamp);
        database = FirebaseDatabase.getInstance();

        //Turn off/on light

        led = database.getReference("Led");

        led.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((long) dataSnapshot.getValue() == 1) {
                    lampImg.setImageResource(R.drawable.light_on);
                    isTurned = true;
                } else {
                    lampImg.setImageResource(R.drawable.light_off);
                    isTurned = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Error", databaseError.getMessage());
            }
        });

        lampImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTurned) {
                    lampImg.setImageResource(R.drawable.light_off);
                    isTurned = false;
                    led.setValue(0);
                } else {
                    lampImg.setImageResource(R.drawable.light_on);
                    isTurned = true;
                    led.setValue(1);
                }
                Log.d("------------", "---------------------------------`" + isTurned);

            }
        });

        //Choose color

        rBar = getActivity().findViewById(R.id.seekBar_Red);
        gBar = getActivity().findViewById(R.id.seekBar_Green);
        bBar = getActivity().findViewById(R.id.seekBar_Blue);


        rBar.setOnSeekBarChangeListener(seekBarChangeListener);
        gBar.setOnSeekBarChangeListener(seekBarChangeListener);
        bBar.setOnSeekBarChangeListener(seekBarChangeListener);

        redColor = database.getReference("RedColor");
        greenColor = database.getReference("GreenColor");
        blueColor = database.getReference("BlueColor");

        redColor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long value = (long) dataSnapshot.getValue();
                rBar.setProgress((int) value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Error", databaseError.getMessage());
            }
        });

        greenColor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long value = (long) dataSnapshot.getValue();
                gBar.setProgress((int) value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Error", databaseError.getMessage());
            }
        });

        blueColor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long value = (long) dataSnapshot.getValue();
                bBar.setProgress((int) value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Error", databaseError.getMessage());
            }
        });

        //Photoresistor:

        light = database.getReference("Photoresistor");
        lightTxt = getActivity().findViewById(R.id.light_level);

        light.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long value = (long) dataSnapshot.getValue();
                String s = "Уровень освещения\n" + value + " ед.";
                lightTxt.setText(s);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    redColor.setValue(rBar.getProgress());
                    greenColor.setValue(gBar.getProgress());
                    blueColor.setValue(bBar.getProgress());
                }
            }, 100);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };
}
