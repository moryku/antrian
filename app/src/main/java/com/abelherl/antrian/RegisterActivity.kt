package com.abelherl.antrian

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.abelherl.antrian.Model.ProfilModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    lateinit var ref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance().reference.child("User")

        btnRegister.setOnClickListener {
            createEmailPassword()
        }

        toLogin.setOnClickListener {
            intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun createEmailPassword() {
        val email = emailRegister.text.toString()
        val password = passwordRegister.text.toString()

        if (email == "") {
            Toast.makeText(this, "Please Write Email.", Toast.LENGTH_LONG).show()
        }
        else if (password == "") {
            Toast.makeText(this, "Please Write Password.", Toast.LENGTH_LONG).show()
        }
        else {
             mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val profile = ProfilModel(email, password, "", "", "", "")
                        ref.child(mAuth.getCurrentUser()!!.getUid()).setValue(profile).addOnCompleteListener { task ->
                            if(task.isSuccessful){
                                //Toast.makeText(this, "Data Berhasil Disimpan", Toast.LENGTH_SHORT).show()
                                adminCheck(mAuth.currentUser!!.uid)
                            }
                            else{
                                Toast.makeText(this@RegisterActivity, "Error Message:" + task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        Toast.makeText(this@RegisterActivity, "Error Message:" + task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    private fun adminCheck(uid: String) {
        FirebaseDatabase.getInstance().getReference("User").child(uid).addListenerForSingleValueEvent( object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var intent = Intent()
                if (dataSnapshot.child("level").value.toString() == "admin") {
                    intent = Intent(this@RegisterActivity, AdminAdminHome::class.java)
                }
                else {
                    intent = Intent(this@RegisterActivity, MainActivity::class.java)
                }
                goTo(this@RegisterActivity, intent, true)
            }

        })
    }
}