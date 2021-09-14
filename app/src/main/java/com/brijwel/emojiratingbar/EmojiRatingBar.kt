package com.brijwel.emojiratingbar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

class EmojiRatingBar : AppCompatRatingBar {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val iconArrayActive = intArrayOf(
        R.drawable.ic_feedback_01,
        R.drawable.ic_feedback_02,
        R.drawable.ic_feedback_03,
        R.drawable.ic_feedback_04,
        R.drawable.ic_feedback_05
    )

    private val iconArrayInactive = intArrayOf(
        R.drawable.ic_feedback_01_grey,
        R.drawable.ic_feedback_02_grey,
        R.drawable.ic_feedback_03_grey,
        R.drawable.ic_feedback_04_grey,
        R.drawable.ic_feedback_05_grey
    )

    init {
        this.max = 5
        this.numStars = 5
        this.stepSize = 1.0f
    }

    private fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
        var drawable = ContextCompat.getDrawable(context, drawableId)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = DrawableCompat.wrap(drawable!!).mutate()
        }

        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    @SuppressLint("DrawAllocation")
    @Synchronized
    override fun onDraw(canvas: Canvas) {
        val stars = numStars
        val rating = rating
        var x: Float

        var bitmap: Bitmap? = null

        val paint = Paint()

        val W = width
        val H = height
        var icon_size = W / stars - 32//21 //(H < W)?(H):(W); //72
        if (icon_size > H - 16) {
            icon_size = H - 16
        }
        val emoji_y_pos = H / 2 - icon_size / 2

        val delta = (if (H > W) H else W) / stars
        val offset = (W - (icon_size + (stars - 1) * delta)) / 2

        for (i in 0 until stars) {
            if (rating.toInt() - 1 == (i)) {
                bitmap = getBitmapFromVectorDrawable(context, iconArrayActive[i])
                for (j in 0 until (i + 1)) {
                    x = (offset + j * delta).toFloat()
                    val scaled = Bitmap.createScaledBitmap(bitmap, icon_size, icon_size, true)
                    canvas.drawBitmap(scaled, x, emoji_y_pos.toFloat(), paint)
                }
                canvas.save()
            } else {
                bitmap = getBitmapFromVectorDrawable(context, iconArrayInactive[i])
                x = (offset + i * delta).toFloat()
                val scaled = Bitmap.createScaledBitmap(bitmap, icon_size, icon_size, true)
                canvas.drawBitmap(scaled, x, emoji_y_pos.toFloat(), paint)
                canvas.save()
            }
        }
    }
}