package com.example.mephigrowbox;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class FragmentTemp extends Fragment {

    private Handler handler;

    private GraphView gvGraph;
    private LineGraphSeries series;
    private int counter=0;

    private FirebaseDatabase database;
    private DatabaseReference temperature;
    private long value;
    private FirebaseAuth auth;
    FirebaseUser user = auth.getInstance().getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_temp, container, false);

        gvGraph = view.findViewById(R.id.graph);
        series = new LineGraphSeries();

        gvGraph.addSeries(series);
        gvGraph.getViewport().setMinY(0);
        gvGraph.getViewport().setMaxY(40);
        gvGraph.getViewport().setMaxX(30);
        gvGraph.getViewport().setYAxisBoundsManual(true);
        gvGraph.getViewport().setXAxisBoundsManual(true);
        startTimer();

        database = FirebaseDatabase.getInstance();

        temperature = database.getReference().child(user.getUid()).child("Temperature");
        final ArcProgress tempBar = view.findViewById(R.id.temperature_progress_bar);

        temperature.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                value = (long) dataSnapshot.getValue();
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
        return view;
    }
    private void startTimer(){
        handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                temperature.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        value = (long) dataSnapshot.getValue();
                        if (counter>=30) series.appendData(new DataPoint(counter, value), true, 1000);
                        series.appendData(new DataPoint(counter, value), false, 1000);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
                counter++;
                handler.postDelayed(this,1000);
            }
        },1000);
    }
}
