package com.example.zypher;

import android.view.MotionEvent;
import android.view.View;

import com.borutsky.neumorphism.NeumorphicFrameLayout;

import kotlin.reflect.KCallable;

public class animator {
    public NeumorphicFrameLayout neumorphicFrameLayout;
    public NeumorphicFrameLayout.State intialState;

    public animator(NeumorphicFrameLayout neumorphicFrameLayout, NeumorphicFrameLayout.State pressstate, Runnable callback) {
        this.neumorphicFrameLayout = neumorphicFrameLayout;
        intialState = neumorphicFrameLayout.getState();
        neumorphicFrameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    neumorphicFrameLayout.setState(pressstate);
                }
                if (motionEvent.getAction()==MotionEvent.ACTION_UP) {
                    neumorphicFrameLayout.setState(intialState);
                    try {
                        callback.run();
                    } catch (Exception e) {

                    }
                }
                return true;
            }
        });
    }
}
