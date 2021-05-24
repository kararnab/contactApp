package com.kararnab.contacts

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kararnab.contacts.callbacks.PermissionsCallback

object UiUtils {

    fun checkCallPermission(context: Context,callback: PermissionsCallback) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Camera permission has not been granted.
            callback.onRejected()
        } else {
            // Permission is already available, now you can call.
            callback.onGranted()
        }
    }

    fun requestCallPermission(activity: Activity) {
        // https://github.com/tbruyelle/RxPermissions, we can simplify it later using rxPermissions
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.CALL_PHONE
            )
        ) {
            // Provide an additional rationale to the user if the permission was not granted,
            // user has previously denied the permission.
            Toast.makeText(activity, R.string.manual_call_perm_request, Toast.LENGTH_LONG).show()
        } else {
            // Call permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.CALL_PHONE),
                ContactViewActivity.REQUEST_CALL
            )
        }
    }

    fun callPhoneNumber(context: Context, phoneNo: String?, isAdminPriviledge: Boolean) {
        if (isAdminPriviledge) {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$phoneNo")
            context.startActivity(intent) //TODO: Android M Runtime Permissions
        } else {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phoneNo")
            context.startActivity(intent)
        }
    }

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

    @JvmStatic
    fun expand(v: View) {
        v.measure(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        val targetHeight = v.measuredHeight
        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.layoutParams.height = 1
        v.visibility = View.VISIBLE
        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                v.layoutParams.height =
                    if (interpolatedTime == 1.0f) WindowManager.LayoutParams.WRAP_CONTENT else (targetHeight * interpolatedTime).toInt()
                v.requestLayout();
            }
            override fun willChangeBounds(): Boolean {
                return true
            }
        }
        // 1dp/ms
        a.duration = (targetHeight / v.context.resources.displayMetrics.density).toLong()
        v.startAnimation(a)
    }

    @JvmStatic
    fun collapse(v: View) {
        val initialHeight = v.measuredHeight
        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                if(interpolatedTime == 1.0f){
                    v.visibility = View.GONE;
                }else{
                    v.layoutParams.height = (initialHeight - (initialHeight * interpolatedTime).toInt())
                    v.requestLayout();
                }
            }
            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        a.duration = ((initialHeight / v.context.resources.displayMetrics.density).toLong());
        v.startAnimation(a)
    }
}