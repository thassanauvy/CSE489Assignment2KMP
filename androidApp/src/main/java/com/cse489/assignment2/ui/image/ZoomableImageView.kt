package com.cse489.assignment2.ui.image

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.max
import kotlin.math.min

class ZoomableImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var zoom = 1f

    private val scaleDetector = ScaleGestureDetector(
        context,
        object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                zoom = min(MAX_ZOOM, max(MIN_ZOOM, zoom * detector.scaleFactor))
                pivotX = detector.focusX
                pivotY = detector.focusY
                scaleX = zoom
                scaleY = zoom
                return true
            }
        },
    )

    init {
        isClickable = true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleDetector.onTouchEvent(event)
        return true
    }

    fun resetZoom() {
        zoom = 1f
        scaleX = 1f
        scaleY = 1f
        pivotX = width / 2f
        pivotY = height / 2f
    }

    private companion object {
        const val MIN_ZOOM = 1f
        const val MAX_ZOOM = 5f
    }
}
