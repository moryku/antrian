package com.abelherl.antrian

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.abelherl.antrian.data.AntrianItem
import com.abelherl.antrian.data.KegiatanItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.voela.actrans.AcTrans

fun goTo(context: Context, intent: Intent, finish: Boolean) {
    if (finish) { intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) }
    context.startActivity(intent)
    AcTrans.Builder(context).performFade()
}
fun updateAntrian(antrianItem: AntrianItem, uploadToFirebase: Boolean, create: Boolean) {
    val ref = FirebaseDatabase.getInstance().reference
    val auth = FirebaseAuth.getInstance()

    if (uploadToFirebase) {
        if (create) {
            ref.child("Queue").child(antrianItem.activity_id).push().setValue(antrianItem)
        }
        else {
            ref.child("Queue").child(antrianItem.activity_id).child(antrianItem.id).push().setValue(antrianItem)
        }
    }
}
fun getKegiatan() : ArrayList<KegiatanItem>{
    val ref = FirebaseDatabase.getInstance().reference
    val auth = FirebaseAuth.getInstance()
    var item = KegiatanItem()
    val arr = arrayListOf<KegiatanItem>()

    ref.child("Activity").addValueEventListener(object :
        ValueEventListener {

        override fun onCancelled(p0: DatabaseError) {

        }

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            //Inisialisasi ArrayList
            if (dataSnapshot.hasChildren()) {
                for (snapshot in dataSnapshot.children) {
                    //Mapping data pada DataSnapshot ke dalam objek mahasiswa
                    item = snapshot.getValue(KegiatanItem::class.java)!!
                    item.id = snapshot.key!!
                    arr.add(item)

                    Log.d("TAG", "signInWithCredential:util" + " " + item)
                }
            }
        }
    })

    return arr
}