package com.abelherl.antrian

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_splash.*
import render.animations.Attention
import render.animations.Fade
import render.animations.Render

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT:Long = 1000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initView()
    }

    override fun onBackPressed() { }

    fun initView() {
        Handler().postDelayed({
            val mAuth = FirebaseAuth.getInstance()
            if(mAuth.currentUser != null){
                adminCheck(mAuth.currentUser!!.uid)
            }
            else {
                goTo(this, Intent(this, LoginActivity::class.java), true)
            }
        }, SPLASH_TIME_OUT)
    }

    private fun adminCheck(uid: String) {
        FirebaseDatabase.getInstance().getReference("User").child(uid).addListenerForSingleValueEvent( object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var intent = Intent()
                if (dataSnapshot.child("level").value.toString() == "admin") {
                    intent = Intent(this@SplashActivity, AdminAdminHome::class.java)
                }
                else {
                    intent = Intent(this@SplashActivity, MainActivity::class.java)
                }
                goTo(this@SplashActivity, intent, true)
            }

        })
    }
}
