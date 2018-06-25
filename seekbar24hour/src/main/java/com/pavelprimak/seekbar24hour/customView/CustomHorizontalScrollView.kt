package com.pavelprimak.seekbar24hour.customView

import android.content.Context
import android.util.AttributeSet
import android.widget.HorizontalScrollView


class CustomHorizontalScrollView(context: Context, attrs: AttributeSet) : HorizontalScrollView(context, attrs) {

    private var scrollerTask: Runnable? = null
    private var initialPosition: Int = 0

    private val newCheck = 100

    private var onScrollStoppedListener: OnScrollStoppedListener? = null

    interface OnScrollStoppedListener {
        fun onScrollStopped()
    }

    init {
        scrollerTask = Runnable {
            val newPosition = scrollX
            if (initialPosition - newPosition == 0) {//has stopped

                if (onScrollStoppedListener != null) {
                    onScrollStoppedListener?.onScrollStopped()
                }
            } else {
                initialPosition = scrollX
                this.postDelayed(scrollerTask, newCheck.toLong())
            }
        }
    }

    fun setOnScrollStoppedListener(listener: OnScrollStoppedListener) {
        onScrollStoppedListener = listener
    }

    fun startScrollerTask() {
        initialPosition = scrollX
        this.postDelayed(scrollerTask, newCheck.toLong())
    }

    companion object {
        private val TAG = "MyScrollView"
    }

}