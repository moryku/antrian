package com.abelherl.antrian

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.EditText
import android.widget.Toast
import com.abelherl.antrian.Model.ProfilModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import id.voela.actrans.AcTrans
import kotlinx.android.synthetic.main.activity_manageprofil.*

class ManageProfil : AppCompatActivity() {

    private var NamaWali: EditText? = null
    private var NamaSantri: EditText? = null
    private var NoHp: EditText? = null
    private var Alamat: EditText? = null
    lateinit var ref : DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onBackPressed() {
        buttonBack()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manageprofil)

        NamaWali = findViewById<EditText>(R.id.txtNW)
        NamaSantri = findViewById<EditText>(R.id.txtNS)
        NoHp = findViewById<EditText>(R.id.txtNoHp)
        Alamat = findViewById<EditText>(R.id.txtAlamat)

        val getUserID: String = FirebaseAuth.getInstance()?.getCurrentUser()?.getUid().toString()
        ref = FirebaseDatabase.getInstance().getReference("User").child(getUserID)
        auth = FirebaseAuth.getInstance()


        val currentUser = auth.currentUser

        tmpEmail.setHint(currentUser!!.email)

        btn_logout.setOnClickListener { buttonLogout() }
        ib_back_profile.setOnClickListener { buttonBack() }

        getData()
        btn_simpan.setOnClickListener{ prosesSave() }
    }
    private fun prosesSave() {
        val getNamaWali: String = NamaWali?.getText().toString()
        val getNamaSantri: String = NamaSantri?.getText().toString()
        val getNoHp: String = NoHp?.getText().toString()
        val getAlamat: String = Alamat?.getText().toString()

        if (getNamaWali.isEmpty() || getNamaSantri.isEmpty() || getNoHp.isEmpty() || getAlamat.isEmpty()) {
            Toast.makeText(this@ManageProfil,"Data tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show()
        } else {


            ref.child("namaWali").setValue(getNamaWali)
            ref.child("namaSantri").setValue(getNamaSantri)
            ref.child("noHp").setValue(getNoHp)
            ref.child("alamat").setValue(getAlamat)

            Toast.makeText(this, "Profil Berhasil Diupdate", Toast.LENGTH_SHORT).show()

              val intent = Intent(this, MainActivity::class.java)
              startActivity(intent)
              finish()


        }
    }
    private fun getData(){
       // val getUserID2: String = auth?.getCurrentUser()?.getUid().toString()

        ref.addListenerForSingleValueEvent( object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val getNamaWali = dataSnapshot.child("namaWali").getValue().toString()
                val getNamaSantri = dataSnapshot.child("namaSantri").getValue().toString()
                val getNoHp = dataSnapshot.child("noHp").getValue().toString()
                val getAlamat = dataSnapshot.child("alamat").getValue().toString()

                txtNW.setText(getNamaWali)
                txtNS?.setText(getNamaSantri)
                txtNoHp.setText(getNoHp)
                txtAlamat.setText(getAlamat)

                txtNW.invalidate()
                txtNS.invalidate()
                txtNoHp.invalidate()
                txtAlamat.invalidate()
            }

        })
    }

    private fun buttonBack() {
        super.onBackPressed()
        AcTrans.Builder(this).performFade()
    }

    private fun buttonLogout() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, LoginActivity::class.java)
        goTo(this, intent, true)
    }
}



















