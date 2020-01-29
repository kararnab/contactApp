package com.kararnab.contacts;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.View;

public class UiUtils {

    static void setSwipeRefreshLayoutLoaderColors(SwipeRefreshLayout swipeRefreshLayout){
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public static void crossFade(final View viewToAppear, final View viewToDisappear, long duration){
        viewToAppear.setAlpha(0f);
        viewToAppear.setVisibility(View.VISIBLE);
        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        viewToAppear.animate()
                .alpha(1f)
                .setDuration(duration)
                .setListener(null);
        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        viewToDisappear.animate()
                .alpha(0f)
                .setDuration(duration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        viewToDisappear.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * Generate a material color based on the number
     * @param num Number based on which color will be generated
     */
    public static int materialColor(int num){
        int colors[] = {0xFF861e6f,0xFF5d55cc,0xFF266037,0xFFff7e7e,0xFFff9019};
        return colors[num%5];
    }
}
