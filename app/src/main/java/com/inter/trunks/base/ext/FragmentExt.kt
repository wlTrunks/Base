package ru.mos.polls.kutil

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager

/**
 * Регистрация и снятие броадкастресиверов
 * @param receivers Pair броадкаст и интетфильтер
 * @param register true - для регистрации, false - для снятия
 */
fun Fragment.proceedBroadcastReceiver(receivers: List<Pair<BroadcastReceiver, IntentFilter>>, register: Boolean) {
    context?.let { context ->
        if (receivers.isNotEmpty()) {
            receivers.forEach { pair ->
                with(LocalBroadcastManager.getInstance(context)) {
                    if (register) {
                        registerReceiver(pair.first, pair.second)
                    } else unregisterReceiver(pair.first)
                }
            }
        }
    }
}

fun <T> Fragment.argumentsData(arg: String): T {
    return arguments?.get(arg) as T
}