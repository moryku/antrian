package com.abelherl.antrian.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.abelherl.antrian.DetailKegiatanActivity
import com.abelherl.antrian.R
import com.abelherl.antrian.data.AntrianItem
import com.abelherl.antrian.data.KegiatanItem
import com.abelherl.antrian.data.UserItem
import com.abelherl.antrian.goTo
import kotlinx.android.extensions.LayoutContainer
import kotlin.collections.ArrayList

class KegiatanAdapter(private val context: Context, private val items : ArrayList<KegiatanItem>, private val sharedPreferences: SharedPreferences, private val antrian : ArrayList<ArrayList<AntrianItem>>, private val user : UserItem) :
    RecyclerView.Adapter<KegiatanAdapter.ViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_kegiatan, parent, false)
        )
    override fun getItemCount(): Int {
        return items.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(context, items.get(position), sharedPreferences, antrian, user)
    }
    class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bindItem(context: Context, item: KegiatanItem, sharedPreferences: SharedPreferences, list: ArrayList<ArrayList<AntrianItem>>, user: UserItem) {
            val tv_title = containerView.findViewById<TextView>(R.id.tv_kegiatan_title)
            val tv_desc = containerView.findViewById<TextView>(R.id.tv_kegiatan_desc)
            val tv_status = containerView.findViewById<TextView>(R.id.tv_status_kegiatan)
            val tv_time = containerView.findViewById<TextView>(R.id.tv_kegiatan_time)
            val tv_estimation = containerView.findViewById<TextView>(R.id.tv_estimation_kegiatan)
            val tv_participant = containerView.findViewById<TextView>(R.id.tv_kegiatan_participant)
            val v_dots = containerView.findViewById<CardView>(R.id.v_dots)
            val tv_nomor = containerView.findViewById<TextView>(R.id.tv_nomor_kegiatan)
            val cv_kegiatan = containerView.findViewById<CardView>(R.id.cv_kegiatan)
            val ll_kegiatan = containerView.findViewById<LinearLayout>(R.id.ll_kegiatan)
            val bt_kegiatan = containerView.findViewById<Button>(R.id.bt_kegiatan)

            var listAntrian = arrayListOf<AntrianItem>()

            for (antri in list) {
                for (thing in antri) {
                    if (thing.activity_id == item.id) {
                        listAntrian = antri
                    }
                }

                Log.d("TAG", "Thing: " + list)
            }

            tv_title.text = item.title
            tv_desc.text = item.description

//            var minuteStart = item.start.minutes.toString()
//            var minuteEnd = item.end.minutes.toString()
//
//            if (item.start.minutes.toString().toCharArray().size == 1) { minuteStart = "0" + minuteStart }
//            if (item.end.minutes.toString().toCharArray().size == 1) { minuteEnd = "0" + minuteEnd }

            tv_estimation.text = "±" + item.estimation + " menit/orang"
            tv_time.text = "Waktu pelayanan: \n" + item.start +
                    " - " + item.finish

            var noSekarang = 0
            var noTotal = 0
            var ikut = false

            tv_nomor.visibility = View.GONE
            tv_nomor.invalidate()
            Log.d("TAG", "TIDAK ANTRI: " + tv_nomor.text + listAntrian)

            for (antri in listAntrian) {
                noTotal += 1

                when (antri.status) {
                    "2" -> noSekarang += 1
                    "3" -> noSekarang += 1
                }

                if (!ikut) {
                    if (antri.uid == user.id && antri.status == "2") {
                        tv_nomor.visibility = View.VISIBLE
                        tv_nomor.text = "Nomor Anda: " + noTotal
                        tv_nomor.invalidate()
                        Log.d("TAG", "MASUK ANTRI: " + tv_nomor.text)
                        ikut = true
                    }
                }
            }

            tv_participant.text = "Nomor " + noSekarang + " dari " + noTotal + " orang"

//            if (item.status == 0) {
//                tv_status.text = "Antrian Ditutup"
//                ll_kegiatan.backgroundTintList = ColorStateList.valueOf(context.resources.getColor(R.color.lightRed))
//                cv_kegiatan.backgroundTintList = ColorStateList.valueOf(context.resources.getColor(R.color.orange))
//            }
//            else {
//                tv_status.text = "Antrian Dibuka"
//                ll_kegiatan.backgroundTintList = ColorStateList.valueOf(context.resources.getColor(R.color.colorPrimaryDark))
//                cv_kegiatan.backgroundTintList = ColorStateList.valueOf(context.resources.getColor(R.color.green))
//            }

            if (item.status == "0") {
                tv_status.text = "Antrian Ditutup"
                v_dots.backgroundTintList = ColorStateList.valueOf(context.resources.getColor(R.color.lightRed))
            }
            else {
                tv_status.text = "Antrian Dibuka"
                v_dots.backgroundTintList = ColorStateList.valueOf(context.resources.getColor(R.color.lightGreen))
            }

            bt_kegiatan.setOnClickListener {
                if (item.status == "0") {
                    Toast.makeText(context, "Maaf, antrian sudah ditutup", Toast.LENGTH_LONG).show()
                }
                else {
                    val intent = Intent(context, DetailKegiatanActivity::class.java)

                    intent.putExtra("id", item.id)
                    intent.putExtra("title", item.title)
                    intent.putExtra("description", item.description)
                    intent.putExtra("estimation", tv_estimation.text.toString())
                    intent.putExtra("time", tv_time.text.toString())
                    intent.putExtra("participant", tv_participant.text.toString())

                    goTo(context, intent, false)
                }
            }
        }
    }
}