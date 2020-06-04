package com.example.mephigrowbox;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.android.material.circularreveal.CircularRevealRelativeLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FragmentTemp extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference temperature;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_temp, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        database = FirebaseDatabase.getInstance();

        temperature = database.getReference("Temperature");
        final ArcProgress tempBar = getActivity().findViewById(R.id.temperature_progress_bar);

        temperature.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long value = (long) dataSnapshot.getValue();
                if (value<10) {
                    tempBar.setBottomText("Холодно!");
                    int color = Color.parseColor("#ff007DD6");
                    tempBar.setFinishedStrokeColor(color);
                }
                else {
                    if (value <= 20 && value >= 10) {
                        int color = Color.parseColor("#ff00ff00");
                        tempBar.setBottomText("Идеально!");
                        tempBar.setFinishedStrokeColor(color);

                    }
                    else {
                        tempBar.setBottomText("Жарко!");
                        int color = Color.parseColor("#ffff0000");
                        tempBar.setFinishedStrokeColor(color);
                    }
                }


                ValueAnimator animator = ValueAnimator.ofInt(tempBar.getProgress(), (int) value);
                animator.setDuration(1000);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        tempBar.setProgress((int) animation.getAnimatedValue());
                    }
                });
                animator.start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}
