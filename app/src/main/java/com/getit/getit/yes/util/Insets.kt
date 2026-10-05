package com.getit.getit.yes.util

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

/** Pads this view so its content stays clear of system bars, display cutouts and the keyboard. */
fun View.padForSystemBars() {
    val start = paddingLeft
    val top = paddingTop
    val end = paddingRight
    val bottom = paddingBottom
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val bars = insets.getInsets(
            WindowInsetsCompat.Type.systemBars() or
                WindowInsetsCompat.Type.displayCutout() or
                WindowInsetsCompat.Type.ime()
        )
        view.updatePadding(start + bars.left, top + bars.top, end + bars.right, bottom + bars.bottom)
        WindowInsetsCompat.CONSUMED
    }
}
