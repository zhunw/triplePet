package com.example.a91927.triplepet.util;


import android.animation.TypeEvaluator;

public class PetPosiEvaluator implements TypeEvaluator {
    @Override
    public Object evaluate(float fraction, Object start, Object end) {
        PetPosiValue startObj = (PetPosiValue) start;
        PetPosiValue endObj = (PetPosiValue) end;
        float x = (startObj.x + fraction * (endObj.x- startObj.x));
        float y = (startObj.y + fraction * (endObj.y- startObj.y));
        PetPosiValue petValues = new PetPosiValue(x, y);
        return petValues;
    }
}
