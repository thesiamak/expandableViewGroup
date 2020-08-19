package ir.drax.expandableViewGroup.widget;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Constraints;
import androidx.core.content.res.ResourcesCompat;

import java.util.Observable;
import java.util.Observer;

import ir.drax.expandableViewGroup.R;

public class Expandable extends ConstraintLayout implements Observer {
    private StateObservable observableState=new StateObservable(false);
    private final int COLLAPSE_DURATION=250, EXPAND_DURATION=320;
    private final int MARGIN_START=16;
    private int expandedHeight=0, collapsedHeight=0;


    public Expandable(Context context) {
        super(context);
        init();
    }

    public Expandable(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Expandable(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void addChild(View...  views){
        for (View view : views) {
            ConstraintLayout.LayoutParams params=new Constraints.LayoutParams(0,LayoutParams.WRAP_CONTENT);
            params.setMarginStart(MARGIN_START);
            params.startToStart=LayoutParams.PARENT_ID;
            params.endToEnd=LayoutParams.PARENT_ID;
            addView(view,params);
        }
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.expandable_layout,this,true);
        observableState.addObserver(this);
        findViewById(R.id.header).setOnClickListener(v->{
            observableState.setState(!observableState.getState());
        });



        addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int top, int i2, int bottom, int i4, int i5, int i6, int oldBottom) {
                if (collapsedHeight>0 && expandedHeight>0) {
                    removeOnLayoutChangeListener(this);
                    collapse();

                }else if (collapsedHeight ==0) {
                    collapsedHeight = bottom-top;
                    expand();
                }else if (expandedHeight ==0) {
                    expandedHeight= bottom-top;
                }
            }
        });
    }

    private void expand(){
        findViewById(R.id.header_icon).setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.expandable_header_enabled,null));
        ConstraintLayout root = findViewById(R.id.root);

        if (expandedHeight==0) {
            expandConnect(root,false);
        }
        else{
            ResizeAnimation resizeAnimation = new ResizeAnimation(root, expandedHeight, collapsedHeight,interpolate -> {

            });
            resizeAnimation.setDuration(EXPAND_DURATION);
            resizeAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    expandConnect(root,true);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            root.startAnimation(resizeAnimation);
        }
    }
    private void expandConnect(ConstraintLayout root,boolean animate){

        if (animate){
            ChangeBounds transition=new ChangeBounds();
            transition.setInterpolator(new TimeInterpolator() {
                @Override
                public float getInterpolation(float interpolate) {
                    findViewById(R.id.container1).setAlpha(interpolate);
                    findViewById(R.id.container2).setAlpha( interpolate);
                    return interpolate;
                }
            });
            TransitionManager.beginDelayedTransition(root,transition);
        }

        findViewById(R.id.container1).setVisibility(VISIBLE);
        findViewById(R.id.container2).setVisibility(VISIBLE);

        ConstraintSet before = new ConstraintSet();
        before.clone(root);
        before.connect(R.id.container1, ConstraintSet.TOP, R.id.header, ConstraintSet.BOTTOM);
        before.setMargin(R.id.container2, ConstraintSet.TOP, 0);
        before.connect(R.id.container2, ConstraintSet.TOP, R.id.container1, ConstraintSet.BOTTOM);

        before.applyTo(root);
    }

    private void collapse(){
        findViewById(R.id.header_icon).setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.expandable_header_disabled,null));
        ConstraintLayout root = findViewById(R.id.root);


        TransitionManager.beginDelayedTransition(this);

        ConstraintSet before = new ConstraintSet();
        before.clone(root);
        before.connect(R.id.container1,ConstraintSet.TOP,R.id.header,ConstraintSet.TOP);
        before.connect(R.id.container2,ConstraintSet.TOP,R.id.container1,ConstraintSet.TOP);
        before.setMargin(R.id.container2,ConstraintSet.TOP,0);

        before.applyTo(root);

        if (collapsedHeight > 0) {
            ResizeAnimation resizeAnimation = new ResizeAnimation(root, collapsedHeight, expandedHeight,
                    interpolate -> {
                        if (interpolate==1){
                            findViewById(R.id.container1).setVisibility(GONE);
                            findViewById(R.id.container2).setVisibility(GONE);
                        }else {
                            findViewById(R.id.container1).setAlpha(1 - interpolate);
                            findViewById(R.id.container2).setAlpha(1 - interpolate);
                        }
                    });
            resizeAnimation.setDuration(COLLAPSE_DURATION);
            root.startAnimation(resizeAnimation);
        }

    }


    @Override
    public void update(Observable observable, Object o) {
        if((Boolean) o)
            expand();
        else
            collapse();

    }
}
