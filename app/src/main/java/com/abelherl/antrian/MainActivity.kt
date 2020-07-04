package com.abelherl.antrian

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    lateinit var ref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()

        val getUserID: String = FirebaseAuth.getInstance()?.getCurrentUser()?.getUid().toString()
        ref = FirebaseDatabase.getInstance().getReference("User").child(getUserID)

        getData()
        btnLogout.setOnClickListener {
            mAuth.signOut()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        btnManage.setOnClickListener {

            val intent = Intent(this, ManageProfil::class.java)
            startActivity(intent)
        }
    }
    private fun getData(){

        ref.addListenerForSingleValueEvent( object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val getNamaWali = dataSnapshot.child("namaWali").getValue().toString()
                val getNamaSantri = dataSnapshot.child("namaSantri").getValue().toString()
                val getNoHp = dataSnapshot.child("noHp").getValue().toString()
                val getAlamat = dataSnapshot.child("alamat").getValue().toString()

                tampilNW.text = getNamaWali
                tampilNS?.text = getNamaSantri
                tampilNoHp?.text = getNoHp
                tampilAlamat?.text = getAlamat
            }

        })
    }
}