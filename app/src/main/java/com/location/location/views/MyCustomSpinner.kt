package com.location.location.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSpinner
import com.location.location.R


class MyCustomSpinner(mContext: Context, attrs: AttributeSet?) :
    AppCompatSpinner(mContext, attrs) {
    init {

        val array = context.obtainStyledAttributes(attrs, R.styleable.MyCustomSpinner)

        val default = resources.getDimension(R.dimen.default_corner_radius)
        var cornerRadiusTopLeft = default
        var cornerRadiusTopRight = default
        var cornerRadiusBottomRight = default
        var cornerRadiusBottomLeft = default
        val strokeSize = array.getInt(R.styleable.MyCustomSpinner_MySpiBackgroundStrokeSize, 0)
        val color = array.getColor(
            R.styleable.MyCustomSpinner_MySpiBackgroundColor,
            Color.TRANSPARENT/* ContextCompat.getColor(context, R.color.colorWhite)*/
        )
        val strokeColor =
            array.getColor(R.styleable.MyCustomSpinner_MySpiBackgroundStrokeColor, Color.TRANSPARENT)
        cornerRadiusTopLeft =
            array.getDimension(R.styleable.MyCustomSpinner_MySpiCorRadiusTopLeft, cornerRadiusTopLeft)
        cornerRadiusTopRight = array.getDimension(
            R.styleable.MyCustomSpinner_MySpiCorRadiusTopRight,
            cornerRadiusTopRight
        )
        cornerRadiusBottomRight = array.getDimension(
            R.styleable.MyCustomSpinner_MySpiCorRadiusBottomRight,
            cornerRadiusBottomRight
        )
        cornerRadiusBottomLeft = array.getDimension(
            R.styleable.MyCustomSpinner_MySpiCorRadiusBottomLeft,
            cornerRadiusBottomLeft
        )


        if (color != Color.TRANSPARENT || strokeColor != Color.TRANSPARENT)
            background = DrawableHelper().getRoundedRectangleDrawableWithStroke(
                context,
                strokeColor,
                color,
                cornerRadiusTopLeft.toInt(),
                cornerRadiusTopRight.toInt(),
                cornerRadiusBottomRight.toInt(),
                cornerRadiusBottomLeft.toInt(),
                strokeSize
            )

        array.recycle()
    }
}
