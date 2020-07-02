package com.abelherl.antrian.adapter

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.abelherl.antrian.R
import com.abelherl.antrian.data.AntrianItem
import com.abelherl.antrian.data.KegiatanItem
import com.abelherl.antrian.data.UserItem
import id.voela.actrans.AcTrans
import kotlinx.android.extensions.LayoutContainer
import java.util.*
import kotlin.collections.ArrayList
import kotlinx.android.synthetic.main.item_kegiatan.*

class KegiatanAdapter(private val context: Context, private val items : ArrayList<KegiatanItem>, private val antrian : ArrayList<AntrianItem>, private val user : UserItem) :
    RecyclerView.Adapter<KegiatanAdapter.ViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_kegiatan, parent, false)
        )
    override fun getItemCount(): Int {
        return items.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(context, items.get(position), antrian, user)
    }
    class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bindItem(context: Context, item: KegiatanItem, list: ArrayList<AntrianItem>, user: UserItem) {
            val tv_title = containerView.findViewById<TextView>(R.id.tv_kegiatan_title)
            val tv_desc = containerView.findViewById<TextView>(R.id.tv_kegiatan_desc)
            val tv_status = containerView.findViewById<TextView>(R.id.tv_status_kegiatan)
            val tv_time = containerView.findViewById<TextView>(R.id.tv_time_kegiatan)
            val tv_participant = containerView.findViewById<TextView>(R.id.tv_kegiatan_participant)
            val v_dots = containerView.findViewById<CardView>(R.id.v_dots)
            val tv_nomor = containerView.findViewById<TextView>(R.id.tv_nomor_kegiatan)

            tv_title.text = item.title
            tv_desc.text = item.description

            var minuteStart = ""
            var minuteEnd = ""
            if (item.start.minutes.toString().toCharArray().size == 1) { minuteStart = "0" + minuteStart }
            if (item.end.minutes.toString().toCharArray().size == 1) { minuteEnd = "0" + minuteEnd }

            tv_time.text = item.start.hours.toString() + ":" + minuteStart +
                    " - " + item.end.hours.toString() + ":" + minuteEnd

            var noSekarang = 0
            var noTotal = 0
            var ikut = false

            for (antri in list) {
                if (antri.kegiatanId == item.id) {
                    noTotal += 1

                    when (antri.status) {
                        2 -> noSekarang += 1
                    }

                    if (!ikut) {
                        if (antri.userId == user.id) {
                            val nomor = noTotal++
                            tv_nomor.visibility = View.VISIBLE
                            tv_nomor.text = "Nomor Anda: " + nomor
                            tv_nomor.invalidate()
                            Log.d("TAG", "MASUK ANTRI: " + tv_nomor.text)
                            ikut = true
                        } else {
                            tv_nomor.visibility = View.GONE
                            tv_nomor.invalidate()
                            Log.d("TAG", "TIDAK ANTRI: " + tv_nomor.text)
                        }
                    }
                }
            }

            tv_participant.text = "Nomor " + noSekarang + " dari " + noTotal + " orang"

            if (item.status == 0) {
                tv_status.text = "Antrian Ditutup"
                v_dots.backgroundTintList = ColorStateList.valueOf(context.resources.getColor(R.color.lightRed))
            }
            else {
                tv_status.text = "Antrian Dibuka"
                v_dots.backgroundTintList = ColorStateList.valueOf(context.resources.getColor(R.color.lightGreen))
            }
        }
    }
}