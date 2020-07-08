package com.abelherl.antrian

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.abelherl.antrian.data.AntrianItem
import com.abelherl.antrian.data.KegiatanItem
import com.abelherl.antrian.util.notificationHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.voela.actrans.AcTrans
import kotlinx.android.synthetic.main.activity_detail_kegiatan.*
import kotlinx.android.synthetic.main.item_kegiatan.*
import java.util.*


class DetailKegiatanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_kegiatan)
        initView()
    }

    override fun onBackPressed() {
        buttonBack()
    }

    private fun initView() {
        val bundle = intent.extras

        tv_title_detail.text = bundle!!.getString("title")
        tv_time_detail.text = bundle.getString("time")
        tv_estimation_detail.text = bundle.getString("estimation")
        tv_participant_detail.text = bundle.getString("participant")
        tv_desc_detail.text = bundle.getString("description")

        ib_back_kegiatan.setBackgroundColor(Color.TRANSPARENT)
        ib_back_kegiatan.setOnClickListener { buttonBack() }

        bt_ikut_detail.setOnClickListener { buttonIkut(bundle.getString("id")!!) }
//        bt_ikut_detail.setOnClickListener {
//            notificationHelper(this@DetailKegiatanActivity,"Hurray!!! Kamu dapat antrian nomor 10","Tetap sabar menunggu ya!!!")
//        }
        setData(bundle.getString("id")!!)
    }

    private fun buttonBack() {
        super.onBackPressed()
        AcTrans.Builder(this).performFade()
    }

    private fun buttonIkut(id: String) {
        val date = Date().date.toString() + "/" + Date().month.toString() + "/" + Date().year.toString()
        val item = AntrianItem("0", FirebaseAuth.getInstance().currentUser!!.uid, id, date, "0")
        updateAntrian(item, true, true)
    }

    private fun setData(id: String) {
        val ref = FirebaseDatabase.getInstance().reference
        var item: AntrianItem

        ref.child("Queue").addValueEventListener(object :
            ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //Inisialisasi ArrayList
                var listKegiatan = arrayListOf<KegiatanItem>()
                if (dataSnapshot.hasChildren()) {
                    for (snapshot in dataSnapshot.children) {
                        if (snapshot.key == id) {
                            var antrian = 1
                            for (child in snapshot.children) {
                                if (child.key != "status") {
                                    item = child.getValue(AntrianItem::class.java)!!
                                    item.id = child.key!!
                                    try {
                                        if (item.uid == FirebaseAuth.getInstance().currentUser!!.uid && item.status != "3") {
                                            bt_ikut_detail.visibility = View.GONE
                                            rl_antrian_detail.visibility = View.VISIBLE

                                            bt_ikut_detail.invalidate()
                                            rl_antrian_detail.invalidate()

                                            if (item.status == "1") {
                                                tv_status_detail.text = "Nomor Anda: " + antrian
                                            }

                                            bt_batal_detail.setOnClickListener { buttonBatal(item) }
                                        }
                                        if (item.uid == FirebaseAuth.getInstance().currentUser!!.uid && item.status == "3"){
                                            bt_ikut_detail.visibility = View.VISIBLE
                                            rl_antrian_detail.visibility = View.GONE
                                        }
                                    } catch (e: NullPointerException) {
                                        Log.d("TAG", "No User Found: " + e)
                                    }
                                    antrian++
                                }
                            }
                        }
                    }
                }
            }
        })
    }

    private fun buttonBatal(item: AntrianItem) {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Batalkan Antrian")
        builder.setMessage("Anda akan membatalkan antrian anda, apakah anda yakin?")

        builder.setPositiveButton("Ya"
        ) { dialog, which -> // Do nothing but close the dialog
            Toast.makeText(this, "Update data", Toast.LENGTH_SHORT).show()
            item.status = "3"
            updateAntrian(item, true, false)
            dialog.dismiss()
        }

        builder.setNegativeButton("Tidak"
        ) { dialog, which -> // Do nothing
            Toast.makeText(this, "Batal", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        val alert: AlertDialog = builder.create()
        alert.show()
    }

}
