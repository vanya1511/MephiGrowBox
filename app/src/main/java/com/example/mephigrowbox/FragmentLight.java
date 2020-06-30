package com.example.mephigrowbox;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentLight extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference led;
    private DatabaseReference redColor;
    private DatabaseReference greenColor;
    private DatabaseReference blueColor;
    private DatabaseReference power;
    private ImageView lampImg;
    private boolean isTurned = false;
    private SeekBar rBar, gBar, bBar, pBar;
    private TextView lightTxt;
    private Button applyBtn, returnBtn;
    private long redValue, greenValue, blueValue,powerValue;
    private FirebaseAuth auth;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_light, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        applyBtn = getActivity().findViewById(R.id.apply_button);
        returnBtn = getActivity().findViewById(R.id.return_button);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.parseColor("#2EB43B"));
        drawable.setCornerRadius((float) 122.3);

        returnBtn.setBackground(drawable);

        lampImg = getActivity().findViewById(R.id.lamp);
        database = FirebaseDatabase.getInstance();

        //Turn off/on light

        led = database.getReference().child(user.getUid()).child("Led");

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

        rBar = getActivity().findViewById(R.id.seek_bar_red);
        gBar = getActivity().findViewById(R.id.seek_bar_green);
        bBar = getActivity().findViewById(R.id.seek_bar_blue);
        pBar = getActivity().findViewById(R.id.seek_bar_power);


        rBar.setOnSeekBarChangeListener(seekBarChangeListener);
        gBar.setOnSeekBarChangeListener(seekBarChangeListener);
        bBar.setOnSeekBarChangeListener(seekBarChangeListener);
        pBar.setOnSeekBarChangeListener(seekBarChangeListener);

        redColor = database.getReference().child(user.getUid()).child("RedColor");
        greenColor = database.getReference().child(user.getUid()).child("GreenColor");
        blueColor = database.getReference().child(user.getUid()).child("BlueColor");
        power = database.getReference().child(user.getUid()).child("PowerOfLight");

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        power.setValue(pBar.getProgress());
                        redColor.setValue(rBar.getProgress());
                        greenColor.setValue(gBar.getProgress());
                        blueColor.setValue(bBar.getProgress());
                    }
                }, 100);
            }
        });

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pBar.setProgress((int) powerValue);
                rBar.setProgress((int) redValue);
                gBar.setProgress((int) greenValue);
                bBar.setProgress((int) blueValue);
            }
        });


        redColor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                redValue = (long) dataSnapshot.getValue();
                rBar.setProgress((int) redValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Error", databaseError.getMessage());
            }
        });

        greenColor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                greenValue = (long) dataSnapshot.getValue();
                gBar.setProgress((int) greenValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Error", databaseError.getMessage());
            }
        });

        blueColor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                blueValue = (long) dataSnapshot.getValue();
                bBar.setProgress((int) blueValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Error", databaseError.getMessage());
            }
        });

        power.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                powerValue = (long) dataSnapshot.getValue();
                pBar.setProgress((int) powerValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Error", databaseError.getMessage());
            }
        });

        //Photoresistor:

        DatabaseReference light = database.getReference().child(user.getUid()).child("Photoresistor");
        lightTxt = getActivity().findViewById(R.id.light_result);

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
        public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
            int myColor = Color.argb(pBar.getProgress(), rBar.getProgress(), gBar.getProgress(), bBar.getProgress());
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(myColor);
            drawable.setCornerRadius((float) 122.3);
            applyBtn.setBackground(drawable);

            Log.d("---------------","------------------------" + myColor);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };
}