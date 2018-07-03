package com.example.a91927.triplepet.util;

import android.animation.TypeEvaluator;

/**
 * Created by 91927 on 2018/6/30.
 */

public class PetAlphaEvaluator implements TypeEvaluator {
        @Override
        public Object evaluate(float fraction, Object start, Object end) {
            PetAlphaValue startObj = (PetAlphaValue) start;
            PetAlphaValue endObj = (PetAlphaValue) end;
            float x = startObj.alpha + fraction * (endObj.alpha - startObj.alpha);
//            if(x == endObj.alpha)
//                x = startObj.alpha;
            PetAlphaValue petValues = new PetAlphaValue(x);
            return petValues;
        }
}
