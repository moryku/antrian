package com.abelherl.antrian

import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abelherl.antrian.dataclass.Activity
import com.abelherl.antrian.util.Util
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_admin_create_act.*
import kotlinx.android.synthetic.main.activity_admin_manage_act.*
import kotlinx.android.synthetic.main.activity_admin_update_act.*
import java.text.SimpleDateFormat
import java.util.*

class AdminUpdateAct : AppCompatActivity() {

    private lateinit var actRef: DatabaseReference
    private var actId: String? = null
    private val util = Util(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_update_act)

        actId = intent.getStringExtra("actId")
        actRef = FirebaseDatabase.getInstance().getReference("Activity").child(actId.toString())

        setSupportActionBar(bar_upt_act)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        tv_upt_act_start.setOnClickListener {
            showTimePicker("start")
        }

        tv_upt_act_finish.setOnClickListener {
            showTimePicker("finish")
        }

        btn_upt_act.setOnClickListener {
            val getName: String = tv_upt_act_name.text.toString()
            val getTitle: String = tv_upt_act_title.text.toString()
            val getDesc: String = tv_upt_act_desc.text.toString()

            val getStart = tv_upt_act_start.text.toString()
            val getFinish = tv_upt_act_finish.text.toString()
            updateAct(getName, getTitle, getDesc, getStart, getFinish)
            util.toast("Update success")
        }

    }

    private fun showTimePicker(type: String) {
        val calendar: Calendar = Calendar.getInstance()

        val picker = TimePickerDialog.OnTimeSetListener { view, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)

            if (type.equals("start")){
                tv_upt_act_start.setText(SimpleDateFormat("HH:mm").format(calendar.time).toString())
            }else{
                tv_upt_act_finish.setText(SimpleDateFormat("HH:mm").format(calendar.time).toString())
            }
        }
        TimePickerDialog(this, picker, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()

    }

    private fun updateAct(name: String, title: String, desc: String, start: String, finish: String) {
        actRef.child("name").setValue(name).addOnSuccessListener {
            util.createLog("update", "update name success")
        }.addOnFailureListener { exception ->
            util.toast(exception.message.toString())
            util.createLog("db", exception.message.toString())
        }

        actRef.child("description").setValue(desc).addOnSuccessListener {
            util.createLog("update", "update desc success")
        }.addOnFailureListener { exception ->
            util.toast(exception.message.toString())
            util.createLog("db", exception.message.toString())
        }

        actRef.child("start").setValue(start).addOnSuccessListener {
            util.createLog("update", "update time success")
        }.addOnFailureListener { exception ->
            util.toast(exception.message.toString())
            util.createLog("db", exception.message.toString())
        }

        actRef.child("finish").setValue(finish).addOnSuccessListener {
            util.createLog("update", "update time success")
        }.addOnFailureListener { exception ->
            util.toast(exception.message.toString())
            util.createLog("db", exception.message.toString())
        }

        actRef.child("title").setValue(title).addOnSuccessListener {
            util.createLog("update", "update title success")
        }.addOnFailureListener { exception ->
            util.toast(exception.message.toString())
            util.createLog("db", exception.message.toString())
        }

    }

    override fun onStart() {
        super.onStart()
        setField()
    }

    private fun setField() {
        actRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                util.toast(error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val activity = snapshot.getValue(Activity::class.java)
                val dbName: String = activity!!.name
                val dbTitle: String = activity.title
                val dbDesc: String = activity.description
                val dbFinish: String = activity.finish
                val dbStart: String = activity.start

                tv_upt_act_start.setText(dbStart)
                tv_upt_act_finish.setText(dbFinish)
                tv_upt_act_name.setText(dbName)
                tv_upt_act_title.setText(dbTitle)
                tv_upt_act_desc.setText(dbDesc)
            }
        })
    }

}
