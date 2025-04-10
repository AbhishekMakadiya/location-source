package com.location.location.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat

import com.location.location.constants.Const
import com.location.location.R

class MyCustomEditText(context: Context, attrs: AttributeSet?) : AppCompatEditText(context, attrs) {

    init {

        val array = context.obtainStyledAttributes(attrs, R.styleable.MyCustomEditText)
        val myFontFamily = array.getInt(R.styleable.MyCustomEditText_myFontFamily, Const.FONT_TYPE_MEDIUM)

        val default = resources.getDimension(R.dimen.default_corner_radius)
        var cornerRadiusTopLeft = default
        var cornerRadiusTopRight = default
        var cornerRadiusBottomRight = default
        var cornerRadiusBottomLeft = default
        val strokeSize = array.getInt(R.styleable.MyCustomEditText_MyEdtBackgroundStrokeSize, 0)
        val color =
            array.getColor(R.styleable.MyCustomEditText_MyEdtBackgroundColor, Color.TRANSPARENT)
        val strokeColor = array.getColor(
            R.styleable.MyCustomEditText_MyEdtBackgroundStrokeColor,
            Color.TRANSPARENT
        )
        cornerRadiusTopLeft = array.getDimension(
            R.styleable.MyCustomEditText_MyEdtCorRadiusTopLeft,
            cornerRadiusTopLeft
        )
        cornerRadiusTopRight = array.getDimension(
            R.styleable.MyCustomEditText_MyEdtCorRadiusTopRight,
            cornerRadiusTopRight
        )
        cornerRadiusBottomRight = array.getDimension(
            R.styleable.MyCustomEditText_MyEdtCorRadiusBottomRight,
            cornerRadiusBottomRight
        )
        cornerRadiusBottomLeft = array.getDimension(
            R.styleable.MyCustomEditText_MyEdtCorRadiusBottomLeft,
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
        applyFontFamily(myFontFamily)
    }

    private fun applyFontFamily(myFontFamily: Int) {
        when (myFontFamily) {
            Const.FONT_TYPE_REGULAR -> {
                typeface = ResourcesCompat.getFont(context, R.font.roboto_regular)
            }
            Const.FONT_TYPE_MEDIUM -> {
                typeface = ResourcesCompat.getFont(context, R.font.roboto_medium)
            }
            Const.FONT_TYPE_SEMI_BOLD -> {
                typeface = ResourcesCompat.getFont(context, R.font.roboto_bold)
            }
            Const.FONT_TYPE_BOLD -> {
                typeface = ResourcesCompat.getFont(context, R.font.roboto_bold)
            }
        }
        this.includeFontPadding=false
    }

}
