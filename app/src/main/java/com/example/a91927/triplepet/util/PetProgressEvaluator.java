package com.example.a91927.triplepet.util;

import android.animation.TypeEvaluator;

public class PetProgressEvaluator implements TypeEvaluator{
    @Override
    public Object evaluate(float fraction, Object start, Object end) {
        PetProgressValue startObj = (PetProgressValue) start;
        PetProgressValue endObj = (PetProgressValue) end;
        float x = startObj.progress + fraction * (endObj.progress - startObj.progress);
        PetProgressValue petValues = new PetProgressValue(x);
        return petValues;
    }
}
