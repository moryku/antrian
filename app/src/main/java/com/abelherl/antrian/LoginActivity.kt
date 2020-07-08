package com.abelherl.antrian

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_manageprofil.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener {
            loginEmailPassword()
        }

        toRegister.setOnClickListener {
            intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
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
                    intent = Intent(this@LoginActivity, AdminAdminHome::class.java)
                }
                else {
                    intent = Intent(this@LoginActivity, MainActivity::class.java)
                }
                goTo(this@LoginActivity, intent, true)
            }

        })
    }

    private fun loginEmailPassword() {
        val email = emailLogin.text.toString()
        val password = passwordLogin.text.toString()

        if (email == "") {
            Toast.makeText(this@LoginActivity, "Please Write Email.", Toast.LENGTH_LONG).show()
        }
        else if (password == "") {
            Toast.makeText(this@LoginActivity, "Please Write Password.", Toast.LENGTH_LONG).show()
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        moveNextActivity()
                    } else {
                        Toast.makeText(this@LoginActivity, "Error Message:" + task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    private fun moveNextActivity() {
        var currentUser = FirebaseAuth.getInstance().currentUser
        if(currentUser != null){
            adminCheck(currentUser.uid)
        }
    }
}