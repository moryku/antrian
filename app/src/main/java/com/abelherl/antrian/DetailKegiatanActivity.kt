package com.abelherl.antrian

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail_kegiatan.*

class DetailKegiatanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_kegiatan)
        initView()
    }

    private fun initView() {
        val bundle = intent.extras

        tv_title_detail.text = bundle!!.getString("title")
        tv_time_detail.text = bundle.getString("time")
        tv_estimation_detail.text = bundle.getString("estimation")
        tv_participant_detail.text = bundle.getString("participant")
        tv_desc_detail.text = bundle.getString("description")
    }
}
