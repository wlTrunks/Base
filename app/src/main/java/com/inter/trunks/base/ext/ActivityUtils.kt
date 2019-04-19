package ru.mos.polls.kutil

import android.os.Build
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import ru.mos.polls.R

/**
 * Установка цвета на туулбар, статусбар и навигационныйбар
 */
fun FragmentActivity.setViewSystemColor(toolbarColor: Int, statusBarColor: Int) {
    val window = this.window
    val toolbar = this.findViewById(R.id.toolbar) as? Toolbar
    toolbar?.setBackgroundColor(ContextCompat.getColor(this, toolbarColor))
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.statusBarColor = ContextCompat.getColor(this, statusBarColor)
        window.navigationBarColor = ContextCompat.getColor(this, toolbarColor)
    }
}

fun FragmentActivity.setViewSystemColor(color: Int) {
    setViewSystemColor(color, color)
}

