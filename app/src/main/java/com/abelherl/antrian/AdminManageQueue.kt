package com.abelherl.antrian

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abelherl.antrian.dataclass.Activity
import com.abelherl.antrian.dataclass.Queue
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_admin_manage_queue.*
import kotlinx.android.synthetic.main.list_adm_req_rev.view.*

class AdminManageQueue : AppCompatActivity() {

    private lateinit var queRef: DatabaseReference
    private lateinit var adapter: FirebaseRecyclerAdapter<Queue, ListHolder>
    private lateinit var query: Query
    private lateinit var getId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_manage_queue)

        getId = intent.getStringExtra("actId")

        val layoutManager = LinearLayoutManager(this)
        layoutManager.isSmoothScrollbarEnabled = true
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_mng_que.layoutManager = layoutManager

        setSupportActionBar(bar_mng_que)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        queRef = FirebaseDatabase.getInstance().getReference("Queue").child(getId)

    }

    override fun onStart() {
        super.onStart()
        query = queRef.orderByChild("status").equalTo("1")
        setList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_adm_que, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.all_adm_que -> {
                query = queRef.orderByChild("status").startAt("1").endAt("3")
                setList()
            }
            R.id.wait_adm_que -> {
                query = queRef.orderByChild("status").equalTo("1")
                setList()
            }
            R.id.cancel_adm_que -> {
                query = queRef.orderByChild("status").equalTo("3")
                setList()
            }
            R.id.complete_adm_que -> {
                query = queRef.orderByChild("status").equalTo("2")
                setList()
            }
        }

        return super.onOptionsItemSelected(item)
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

                holder.bind(model, reqID)
                holder.itemView.tv_ls_rev_que_no.text = "Antrian no.$count"
                holder.itemView.setOnClickListener {
                    val comAlert = MaterialAlertDialogBuilder(context, android.R.style.ThemeOverlay_Material_Dark)
                    comAlert.setTitle("Complete Request")
                    comAlert.setMessage("Are you sure ?")
                    comAlert.setPositiveButton("Confirm") { dialog, which ->
                        completeQue(reqID)
                    }.setNegativeButton("Cancel"){dialog, which ->

                    }.setNeutralButton("Dismiss"){dialog, which ->
                        dismissQue(reqID)
                    }

                    if (model.status == "1"){
                        comAlert.show()
                    }else{

                    }
                }
            }

        }
        rv_mng_que.adapter = adapter
    }

    private fun dismissQue(reqID: String) {
        FirebaseDatabase.getInstance().getReference("Queue").child(getId).child(reqID).child("status").setValue("3")
            .addOnSuccessListener {
                toast("Dismissed")
            }.addOnFailureListener {
                toast("Dismiss failed")
                createLog("Dismiss failed", it.message.toString())
            }
    }

    private fun completeQue(reqID: String) {
        FirebaseDatabase.getInstance().getReference("Queue").child(getId).child(reqID).child("status").setValue("2")
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
        fun bind(queue: Queue, id: String){
            val actId: String = queue.activity_id
            setTime(actId, itemView)

            var status: String = queue.status

            status = when (status) {
                "1" -> {
                    "Sedang Antri"
                }
                "2" -> {
                    "Antrian Selesai"
                }
                else -> {
                    "Antrian Dibatalkan"
                }
            }

            itemView.tv_ls_rev_status.text = status
            itemView.tv_ls_rev_date.text = queue.time
        }

        private fun setTime(actId: String, itemView: View) {
            FirebaseDatabase.getInstance().getReference("Activity").child(actId)
                .addListenerForSingleValueEvent(object: ValueEventListener{
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

}
