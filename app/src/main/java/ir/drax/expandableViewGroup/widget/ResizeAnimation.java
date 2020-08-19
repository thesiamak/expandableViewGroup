package ir.drax.expandableViewGroup.widget;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ResizeAnimation extends Animation {
    final int targetHeight;
    View view;
    int startHeight;
    private Interpolate interpolate;

    public ResizeAnimation(View view, int targetHeight, int startHeight,Interpolate interpolate) {
        this.view = view;
        this.targetHeight = targetHeight;
        this.startHeight = startHeight;
        this.interpolate = interpolate;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int newHeight = (int) (startHeight+(targetHeight - startHeight) * interpolatedTime);
        //to support decent animation, change new heigt as Nico S. recommended in comments
        //int newHeight = (int) (startHeight+(targetHeight - startHeight) * interpolatedTime);
        interpolate.interpolate(interpolatedTime);
        view.getLayoutParams().height = newHeight;
        view.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}