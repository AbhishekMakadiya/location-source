package com.location.location.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.content.res.ResourcesCompat
import com.location.location.constants.Const
import com.location.location.R


class MyCustomRadioButton(mContext: Context, attrs: AttributeSet?) : AppCompatRadioButton(mContext, attrs) {
    //    private val TAG: String = this.javaClass.simpleName
    init {
//        val textStyle = attrs?.getAttributeIntValue(Const.ANDROID_SCHEMA, "textStyle", Typeface.NORMAL)

        val array = context.obtainStyledAttributes(attrs, R.styleable.MyCustomRadioButton)
        val myFontFamily = array.getInt(R.styleable.MyCustomTextView_myFontFamily, Const.FONT_TYPE_MEDIUM)
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
