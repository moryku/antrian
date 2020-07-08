package com.abelherl.antrian

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.abelherl.antrian.Model.ProfilModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    lateinit var ref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance().getReference("User")

        if(mAuth.getCurrentUser() != null){
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        else{ }

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

        if(email == "" || password == ""){
            Toast.makeText(this@RegisterActivity, "Isilah bre", Toast.LENGTH_SHORT).show()
        }
        else {
             mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val profile = ProfilModel(email, password, "", "", "", "")
                        ref.child(mAuth.getCurrentUser()!!.getUid()).setValue(profile).addOnCompleteListener { task ->
                            if(task.isSuccessful){
                                //Toast.makeText(this, "Data Berhasil Disimpan", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                finish()
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
}