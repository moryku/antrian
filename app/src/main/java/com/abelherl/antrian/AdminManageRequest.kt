package com.abelherl.antrian

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abelherl.antrian.dataclass.Activity
import com.abelherl.antrian.dataclass.Queue
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import kotlinx.android.synthetic.main.activity_admin_manage_request.*
import kotlinx.android.synthetic.main.list_req.view.*

class AdminManageRequest : AppCompatActivity() {

    private lateinit var queRef: DatabaseReference
    private lateinit var adapter: FirebaseRecyclerAdapter<Queue, ListHolder>
    private lateinit var query: Query

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_manage_request)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.isSmoothScrollbarEnabled = true
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_mng_req.layoutManager = layoutManager

        setSupportActionBar(bar_mng_req)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        queRef = FirebaseDatabase.getInstance().getReference("Queue")
    }

    override fun onStart() {
        super.onStart()
        query = queRef.orderByChild("status").equalTo("0")
        setList()
    }

    private fun setList() {
        val option = FirebaseRecyclerOptions.Builder<Queue>()
            .setQuery(query, Queue::class.java)
            .setLifecycleOwner(this)
            .build()
        adapter = object : FirebaseRecyclerAdapter<Queue, ListHolder>(option){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
                val view = layoutInflater.inflate(R.layout.list_req, parent, false)
                return ListHolder(view)
            }

            override fun onBindViewHolder(holder: ListHolder, position: Int, model: Queue) {
                val context = holder.itemView.context
                val reqID = getRef(position).key.toString()

                holder.bind(model)
                holder.itemView.setOnClickListener {
                    val comAlert = MaterialAlertDialogBuilder(context)
                    comAlert.setTitle("Confirm Request")
                    comAlert.setMessage("Are you sure ?")
                    comAlert.setPositiveButton("Confirm") { dialog, which ->
                        completeQue(reqID)
                    }.setNegativeButton("Cancel"){dialog, which ->

                    }.setNeutralButton("Dismiss"){dialog, which ->
                        dismissQue(reqID)
                    }
                    comAlert.show()
                }
            }

        }
        rv_mng_req.adapter = adapter
    }

    private fun dismissQue(reqID: String) {
        FirebaseDatabase.getInstance().getReference("Queue").child(reqID).child("status").setValue("2")
            .addOnSuccessListener {
                toast("Dismissed")
            }.addOnFailureListener {
                toast("Dismiss failed")
                createLog("Dismiss failed", it.message.toString())
            }
    }

    private fun completeQue(reqID: String) {
        FirebaseDatabase.getInstance().getReference("Queue").child(reqID).child("status").setValue("1")
            .addOnSuccessListener {
                toast("Update success")
            }.addOnFailureListener {
                toast("Update failed")
                createLog("update", it.message.toString())
            }
    }

    private fun createLog(s: String, message: String?) {
        Log.d(s, message)
    }

    private fun toast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    class ListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(queue: Queue){
            itemView.tv_ls_req_status.text = queue.status
            itemView.tv_ls_req_time.text = queue.time
        }
    }

}
