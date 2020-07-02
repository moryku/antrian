package com.abelherl.antrian

import android.app.TimePickerDialog
import android.icu.lang.UCharacter.JoiningGroup.E
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.abelherl.antrian.dataclass.Activity
import com.abelherl.antrian.util.Util
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_admin_create_act.*
import java.lang.Math.E
import java.text.SimpleDateFormat
import java.util.*

class AdminCreateAct : AppCompatActivity() {

    private val util = Util(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_create_act)

        setSupportActionBar(bar_crt_act)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        tv_crt_act_start.setOnClickListener {
            showTimePicker("start")
        }

        tv_crt_act_finish.setOnClickListener {
            showTimePicker("finish")
        }

        btn_crt_act.setOnClickListener {
            val getStart = tv_crt_act_start.text.toString()
            val getFinish = tv_crt_act_finish.text.toString()

            val getName = tv_crt_act_name.text.toString()
            val getTitle = tv_crt_act_title.text.toString()
            val getDesc = tv_crt_act_desc.text.toString()
            createNewActivity(getName, getTitle, getDesc, getStart, getFinish)
        }

    }

    private fun showTimePicker(type: String) {
        val calendar: Calendar = Calendar.getInstance()

        val picker = TimePickerDialog.OnTimeSetListener { view, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)

            if (type.equals("start")){
                tv_crt_act_start.setText(SimpleDateFormat("HH:mm").format(calendar.time).toString())
            }else{
                tv_crt_act_finish.setText(SimpleDateFormat("HH:mm").format(calendar.time).toString())
            }
        }
        TimePickerDialog(this, picker, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()

    }

    private fun createNewActivity(name: String, title: String, desc: String, start: String, finish: String) {
        //ambil tanggal hari ini
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        val date = formatter.format(Date())
        //input status
        val status = '0'
        //input parameter method ke dataclass "Activity" sesuai parameter
        val activity = Activity(name, date.toString(), title, desc, status.toString(), start, finish)

        //input db ke activity/$key/{field sesuai parameter dataclass
        FirebaseDatabase.getInstance().getReference("Activity").push().setValue(activity)
            .addOnSuccessListener {
                util.toast("Create success")
                tv_crt_act_title.text = null
                tv_crt_act_name.text = null
                tv_crt_act_desc.text = null
                tv_crt_act_finish.text = null
                tv_crt_act_start.text = null
            }.addOnFailureListener { exception: Exception ->
                util.toast(exception.message.toString())
                util.createLog("create",exception.message.toString())
            }

    }

    private fun loge(s: String, toString: String) {
        Log.d(s, toString)
    }

    private fun toast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }
}
