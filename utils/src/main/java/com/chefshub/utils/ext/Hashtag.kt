package com.chefshub.utils.ext

import android.content.Context
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast


class Hashtag(ctx: Context) : ClickableSpan() {
    var context: Context
    var textPaint: TextPaint? = null
    override fun updateDrawState(ds: TextPaint) {
        textPaint = ds
        ds.color = ds.linkColor
        ds.setARGB(255, 30, 144, 255)
    }

    override fun onClick(widget: View) {
        val tv = widget as TextView
        val s = tv.text as Spanned
        val start = s.getSpanStart(this)
        val end = s.getSpanEnd(this)
        val theWord = s.subSequence(start + 1, end).toString()
        // you can start another activity here
        Toast.makeText(context, String.format("Tag : %s", theWord), 10).show()
    }

    init {
        context = ctx
    }
}