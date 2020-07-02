package com.abelherl.antrian

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_admin_home.*

class AdminAdminHome : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)

        setSupportActionBar(bar_adm_home)

        btn_mng_act.setOnClickListener {
            startActivity(Intent(this, AdminManageActivity::class.java))
        }

        btn_mng_que.setOnClickListener {
            startActivity(Intent(this, AdminManageQueue::class.java))
        }

        btn_mng_req.setOnClickListener {
            startActivity(Intent(this, AdminManageRequest::class.java))
        }

        btn_crt_que.setOnClickListener {
            startActivity(Intent(this, AdminCreateQue::class.java))
        }

    }
}
