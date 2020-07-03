package com.abelherl.antrian

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.abelherl.antrian.Model.ProfilModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_manageprofil.*

class ManageProfil : AppCompatActivity() {

    private var NamaWali: EditText? = null
    private var NamaSantri: EditText? = null
    private var NoHp: EditText? = null
    private var Alamat: EditText? = null
    lateinit var ref : DatabaseReference
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manageprofil)

        NamaWali = findViewById<EditText>(R.id.txtNW)
        NamaSantri = findViewById<EditText>(R.id.txtNS)
        NoHp = findViewById<EditText>(R.id.txtNoHp)
        Alamat = findViewById<EditText>(R.id.txtAlamat)

        ref = FirebaseDatabase.getInstance().getReference("User")
        auth = FirebaseAuth.getInstance()

        btn_simpan.setOnClickListener{
            prosesSave()
        }
    }
    private fun prosesSave() {
        val getNamaWali: String = NamaWali?.getText().toString()
        val getNamaSantri: String = NamaSantri?.getText().toString()
        val getNoHp: String = NoHp?.getText().toString()
        val getAlamat: String = Alamat?.getText().toString()
        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()

        if (getNamaWali.isEmpty() && getNamaSantri.isEmpty() && getNoHp.isEmpty() && getAlamat.isEmpty()) {
            Toast.makeText(this@ManageProfil,"Data tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show()
        } else {

            val profile = ProfilModel("", "", getNamaWali, getNamaSantri, getNoHp, getAlamat)

            ref.child(getUserID).setValue(profile).addOnCompleteListener {
                Toast.makeText(this, "Data Berhasil Disimpan", Toast.LENGTH_SHORT).show()
            }
            //  val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()
        }
    }

}
