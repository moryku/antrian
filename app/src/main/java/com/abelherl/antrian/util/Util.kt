package com.abelherl.antrian.util

import android.content.Context
import android.util.Log
import android.widget.Toast

public class Util (
    private val context: Context
){
    public fun createLog(tag: String, message: String?) {
        Log.d(tag, message)
    }

    public fun toast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}