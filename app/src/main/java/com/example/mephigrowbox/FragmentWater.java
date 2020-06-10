package com.example.mephigrowbox;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.lzyzsd.circleprogress.CircleProgress;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class FragmentWater extends Fragment {

    private Handler handler;

    private GraphView gvGraph;
    private LineGraphSeries series;
    private int counter=0;
    private DatabaseReference humiditySoil;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_water, container, false);

        gvGraph = view.findViewById(R.id.graph);
        series = new LineGraphSeries();

        gvGraph.addSeries(series);
        gvGraph.getViewport().setMinY(0);
        gvGraph.getViewport().setMaxY(1000);
        gvGraph.getViewport().setMaxX(30);
        gvGraph.getViewport().setYAxisBoundsManual(true);
        gvGraph.getViewport().setXAxisBoundsManual(true);
        startTimer();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final CircleProgress soil_circle = view.findViewById(R.id.soil_circle);
        humiditySoil = database.getReference("HumiditySoil");

        humiditySoil.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long value = (long) dataSnapshot.getValue();
                soil_circle.setSuffixText("ед.");
                ValueAnimator animator = ValueAnimator.ofInt(soil_circle.getProgress(), (int) value);
                animator.setDuration(1000);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        soil_circle.setProgress((int) animation.getAnimatedValue());
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
                humiditySoil.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Long value = (long) dataSnapshot.getValue();
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
