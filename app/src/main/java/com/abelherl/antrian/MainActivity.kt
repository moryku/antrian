package com.abelherl.antrian

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.abelherl.antrian.adapter.KegiatanAdapter
import com.abelherl.antrian.data.AntrianItem
import com.abelherl.antrian.data.KegiatanItem
import com.abelherl.antrian.data.UserItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    var listAntrian = arrayListOf<AntrianItem>()
    var user = UserItem("nomi430", "Arifudin", "ajod@gmail.com")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun simulateData() {
//        listKegiatan.add(KegiatanItem("0", "Pembayaran SPP di Pondok Pesantren Al-Hikmah", "Kegiatan antrian bulanan untuk pembayaran SPP di Pondok Pesantren. Silahkan tekan disini untuk mengikuti antrian.", Date(), 5, Date(), Date(), 1))
//        listKegiatan.add(KegiatanItem("1", "Pengambilan Ijazah dengan Aturan Mencegah COVID-19", "Kegiatan antrian pengambilan raport untuk semua murid di Pondok Pesantren. Silahkan tekan disini untuk mengikuti antrian.", Date(), 3, Date(), Date(), 0))
//        listAntrian.add(AntrianItem("0", "nomi40", "0", "09:00", "2"))
//        listAntrian.add(AntrianItem("1", "nomi40", "0", "09:00", "1"))
//        listAntrian.add(AntrianItem("2", "nomi430", "0", "09:00", "1"))
//        listAntrian.add(AntrianItem("3", "nomi30", "0", "09:00", "1"))
//        listAntrian.add(AntrianItem("4", "nomi30", "1", "09:00", "2"))
//
//        Log.d("TAG", "Size: " + listKegiatan)
//        Log.d("TAG", "Size: " + listAntrian)
    }

    private fun setData() {
        val ref = FirebaseDatabase.getInstance().reference
        var item: KegiatanItem

        ref.child("Activity").addValueEventListener(object :
            ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //Inisialisasi ArrayList
                var listKegiatan = arrayListOf<KegiatanItem>()
                if (dataSnapshot.hasChildren()) {
                    for (snapshot in dataSnapshot.children) {
                        //Mapping data pada DataSnapshot ke dalam objek mahasiswa
                        item = snapshot.getValue(KegiatanItem::class.java)!!
                        item.id = snapshot.key!!
                        listKegiatan.add(item)

                        Log.d("TAG", "signInWithCredential:util" + " " + item)
                    }
                }

                Log.d("TAG", "Kegiatan: " + listKegiatan)
//        listKegiatan = ArrayList(listKegiatan.sortedBy { it.start })

                val layoutManager = LinearLayoutManager(this@MainActivity)
                val lp: ViewGroup.LayoutParams = rv_main.getLayoutParams()
                val height2: Int = listKegiatan.size * 330

                lp.height = convertDiptoPix(height2)

                rv_main.setLayoutParams(lp)
                rv_main.layoutManager = layoutManager
                rv_main.adapter = KegiatanAdapter(this@MainActivity, listKegiatan, listAntrian, user)
                rv_main.invalidate()

                if (listKegiatan.size != 0) {
                    tv_empty_main.visibility = View.GONE
                }
            }
        })
    }

    private fun initView() {
        setData()
    }

    fun convertDiptoPix(dip: Int): Int {
        val scale: Float = this.resources.displayMetrics.density
        return (dip * scale + 0.5f).toInt()
    }
}
