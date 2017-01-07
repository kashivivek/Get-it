package com.getit.getit.yes;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.os.Build;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.view.ViewGroup;

/**
 * Created by vIvEk on 18-10-2015.
 */

@TargetApi(Build.VERSION_CODES.KITKAT)
public class CustomTransition extends Transition {

    @Override
    public void captureStartValues(TransitionValues values) {}

    @Override
    public void captureEndValues(TransitionValues values) {}

    @Override
    public Animator createAnimator(ViewGroup sceneRoot,
                                   TransitionValues startValues,
                                   TransitionValues endValues) {
        return null;
    }

}