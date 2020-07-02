package com.abelherl.antrian

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import id.voela.actrans.AcTrans

fun goTo(context: Context, activity: AppCompatActivity, finish: Boolean, withExtra: Int? = 99) {
    val intent = Intent(context, activity::class.java)
    if (withExtra != 99) { intent.putExtra("id", withExtra); }
    if (finish) { intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) }
    context.startActivity(intent)
    AcTrans.Builder(context).performFade()
}