package com.abelherl.antrian

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abelherl.antrian.dataclass.Activity
import com.abelherl.antrian.dataclass.Queue
import com.abelherl.antrian.util.sendNotificationByTopic
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_admin_manage_request.*
import kotlinx.android.synthetic.main.list_adm_req_rev.view.*

class AdminManageRequest : AppCompatActivity() {

    private lateinit var queRef: DatabaseReference
    private lateinit var adapter: FirebaseRecyclerAdapter<Queue, ListHolder>
    private lateinit var query: Query
    private lateinit var getId: String
    var queueTitle: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_manage_request)

        getId = intent.getStringExtra("actId")
        setSupportActionBar(bar_mng_req)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.isSmoothScrollbarEnabled = true
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_mng_req.layoutManager = layoutManager

        setSupportActionBar(bar_mng_req)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        queRef = FirebaseDatabase.getInstance().getReference("Queue").child(getId)
    }

    override fun onStart() {
        super.onStart()
        query = queRef.orderByChild("status").equalTo("0")
        setList()
        setTitle(getId)
    }

    private fun setList() {
        val option = FirebaseRecyclerOptions.Builder<Queue>()
            .setQuery(query, Queue::class.java)
            .setLifecycleOwner(this)
            .build()
        adapter = object : FirebaseRecyclerAdapter<Queue, ListHolder>(option){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
                val view = layoutInflater.inflate(R.layout.list_adm_req_rev, parent, false)
                return ListHolder(view)
            }

            override fun onBindViewHolder(holder: ListHolder, position: Int, model: Queue) {
                val context = holder.itemView.context
                val reqID = getRef(position).key.toString()
                val count = position + 1

                holder.bind(model, getId)
                holder.itemView.tv_ls_rev_que_no.text = "Antrian no.$count"
                holder.itemView.setOnClickListener {
                    val comAlert = MaterialAlertDialogBuilder(context)
                    comAlert.setTitle("Confirm Request")
                    comAlert.setMessage("Are you sure ?")
                    comAlert.setPositiveButton("Confirm") { dialog, which ->
                        completeQue(reqID, model.uid)
                    }.setNegativeButton("Cancel"){dialog, which ->

                    }.setNeutralButton("Dismiss"){dialog, which ->
                        dismissQue(reqID, model.uid)
                    }
                    comAlert.show()
                }
            }

        }
        rv_mng_req.adapter = adapter
    }

    private fun dismissQue(reqID: String, uid: String) {
        FirebaseDatabase.getInstance().getReference("Queue").child(getId).child(reqID).child("status").setValue("3")
            .addOnSuccessListener {
                toast("Dismissed")
            }.addOnFailureListener {
                toast("Dismiss failed")
                createLog("Dismiss failed", it.message.toString())
            }
        sendNotificationByTopic(this, uid, "Coba lain kali ya!!!", "Maaf, antrian anda di "+queueTitle+" ditolak")
    }

    private fun completeQue(reqID: String, uid: String) {
        FirebaseDatabase.getInstance().getReference("Queue").child(getId).child(reqID).child("status").setValue("1")
            .addOnSuccessListener {
                toast("Confirmed")
            }.addOnFailureListener {
                toast("Confirmation failed")
                createLog("update", it.message.toString())
            }
        sendNotificationByTopic(this, uid, "Tunggu dengan sabar ya!!!", "Horeee!!! Kamu udah dapat antrian di "+queueTitle)
    }

    private fun createLog(s: String, message: String?) {
        Log.d(s, message)
    }

    private fun toast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    class ListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind( queue: Queue, getId: String){
            setTime(getId, itemView)
            itemView.tv_ls_rev_status.text = "Antrian Ditunda"
            itemView.tv_ls_rev_date.text = queue.time
        }

        private fun setTime(actId: String, itemView: View) {
            FirebaseDatabase.getInstance().getReference("Activity").child(actId)
                .addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        Log.d("getTime", error.message)
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        val getQueue = snapshot.getValue(Activity::class.java)
                        val title = getQueue?.title.toString()
                        val start = getQueue?.start.toString()
                        val finish = getQueue?.finish.toString()

                        itemView.tv_ls_rev_title.text = title
                        itemView.tv_ls_rev_time.text = "$start - $finish"
                    }

                })
        }
    }

    fun setTitle(actId: String){
        FirebaseDatabase.getInstance().getReference("Activity").child(actId)
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    Log.d("getTime", error.message)
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val getQueue = snapshot.getValue(Activity::class.java)
                    val title = getQueue?.title.toString()

                    queueTitle = title
                }

            })
    }

}
