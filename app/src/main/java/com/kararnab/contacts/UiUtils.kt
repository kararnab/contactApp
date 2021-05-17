package com.kararnab.contacts

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

object UiUtils {
    fun setSwipeRefreshLayoutLoaderColors(swipeRefreshLayout: SwipeRefreshLayout) {
        swipeRefreshLayout.setColorSchemeResources(
            android.R.color.holo_blue_light,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
    }

    fun crossFade(viewToAppear: View, viewToDisappear: View, duration: Long) {
        viewToAppear.alpha = 0f
        viewToAppear.visibility = View.VISIBLE
        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        viewToAppear.animate()
            .alpha(1f)
            .setDuration(duration)
            .setListener(null)
        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        viewToDisappear.animate()
            .alpha(0f)
            .setDuration(duration)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    viewToDisappear.visibility = View.GONE
                }
            })
    }

    /**
     * Generate a material color based on the number
     * @param num Number based on which color will be generated
     */
    @JvmStatic
    fun materialColor(num: Int): Int {
        val colors = intArrayOf(-0x79e191, -0xa2aa34, -0xd99fc9, -0x8182, -0x6fe7)
        return colors[num % 5]
    }
}