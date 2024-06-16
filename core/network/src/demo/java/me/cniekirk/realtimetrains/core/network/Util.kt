package me.cniekirk.realtimetrains.core.network

import android.content.Context

fun Context.readJsonFileToString(fileName: String) =
    this.assets.open(fileName).bufferedReader().use { it.readText() }