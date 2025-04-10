package com.location.location.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.location.location.constants.Const
import com.location.location.R


class MyCustomTextView(mContext: Context, attrs: AttributeSet?) :
    AppCompatTextView(mContext, attrs) {
    init {

        val array = context.obtainStyledAttributes(attrs, R.styleable.MyCustomTextView)
        val myFontFamily = array.getInt(R.styleable.MyCustomTextView_myFontFamily, Const.FONT_TYPE_MEDIUM)


        val default = resources.getDimension(R.dimen.default_corner_radius)
        var cornerRadiusTopLeft = default
        var cornerRadiusTopRight = default
        var cornerRadiusBottomRight = default
        var cornerRadiusBottomLeft = default
        val strokeSize = array.getInt(R.styleable.MyCustomTextView_MyBackgroundStrokeSize, 0)
        val color = array.getColor(
            R.styleable.MyCustomTextView_MyBackgroundColor,
            Color.TRANSPARENT/* ContextCompat.getColor(context, R.color.colorWhite)*/
        )
        val strokeColor =
            array.getColor(R.styleable.MyCustomTextView_MyBackgroundStrokeColor, Color.TRANSPARENT)
        cornerRadiusTopLeft =
            array.getDimension(R.styleable.MyCustomTextView_MyCorRadiusTopLeft, cornerRadiusTopLeft)
        cornerRadiusTopRight = array.getDimension(
            R.styleable.MyCustomTextView_MyCorRadiusTopRight,
            cornerRadiusTopRight
        )
        cornerRadiusBottomRight = array.getDimension(
            R.styleable.MyCustomTextView_MyCorRadiusBottomRight,
            cornerRadiusBottomRight
        )
        cornerRadiusBottomLeft = array.getDimension(
            R.styleable.MyCustomTextView_MyCorRadiusBottomLeft,
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
    fun setColor(color:Int,strokeColor:Int,cornerRadiusTopLeft:Float,cornerRadiusTopRight:Float,cornerRadiusBottomRight:Float,cornerRadiusBottomLeft:Float,strokeSize:Int) {
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
