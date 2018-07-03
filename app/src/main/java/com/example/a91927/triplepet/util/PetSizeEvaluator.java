package com.example.a91927.triplepet.util;

import android.animation.TypeEvaluator;

public class PetSizeEvaluator implements TypeEvaluator {
    @Override
    public Object evaluate(float fraction, Object start, Object end) {
        PetSizeValue startObj = (PetSizeValue) start;
        PetSizeValue endObj = (PetSizeValue) end;
        float w = (startObj.W + fraction * (endObj.W- startObj.W));
        float h = (startObj.H + fraction * (endObj.H- startObj.H));
//        if(w == endObj.W) w = startObj.W;
//        if(h == endObj.H) h = startObj.H;
        PetSizeValue petValues = new PetSizeValue(w, h);
        return petValues;
    }
}
