package com.chefshub.utils.ext

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.*
import androidx.fragment.app.Fragment

private const val PROGRESS_TAG = "progressbar"
fun Activity.setLoading() {
    try {
        (window.decorView.findViewById(android.R.id.content) as ViewGroup?)?.setLoading()
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

fun Fragment.setLoading() {
    try {
        (view as ViewGroup?)?.setLoading()
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

fun Activity.stopLoading() {
    try {
        (window.decorView.findViewById(android.R.id.content) as ViewGroup?)?.stopLoading()
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

fun Fragment.stopLoading() {
    try {
        (view as ViewGroup?)?.stopLoading()
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

private fun ViewGroup.setLoading() {
    when (this) {
        is FrameLayout -> {
            val progress = getLoadingProgress(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER
                )
            }
            if (findViewWithTag<View?>(PROGRESS_TAG) == null)
                addView(progress)
        }
        is ConstraintLayout -> {
            val progress = getLoadingProgress(context).apply {
                val constraintSet = ConstraintSet()
                constraintSet.clone(this@setLoading)
                constraintSet.connect(
                    this.id, END, PARENT_ID, END
                ).also {
                    constraintSet.connect(id, TOP, PARENT_ID, TOP)
                }.also {
                    constraintSet.connect(id, START, PARENT_ID, START)
                }.also {
                    constraintSet.connect(id, BOTTOM, PARENT_ID, BOTTOM)
                }
            }
            if (findViewWithTag<View?>(PROGRESS_TAG) == null)
                addView(progress)
        }
        is RelativeLayout -> {
            val progress = getLoadingProgress(context).apply {
                (layoutParams as RelativeLayout.LayoutParams).addRule(RelativeLayout.CENTER_IN_PARENT)
            }
            if (findViewWithTag<View?>(PROGRESS_TAG) == null)
                addView(progress)
        }
        is LinearLayout -> {
            Toast.makeText(context, "Linear not supported", Toast.LENGTH_SHORT).show()
        }
        else -> {
            Toast.makeText(context, "type not supported", Toast.LENGTH_SHORT).show()
        }
    }
}

private fun getLoadingProgress(context: Context) =
    ProgressBar(context).apply { tag = PROGRESS_TAG }

private fun ViewGroup.stopLoading() {
    val view = findViewWithTag<View?>(PROGRESS_TAG)
    if (view != null)
        removeView(view)
}