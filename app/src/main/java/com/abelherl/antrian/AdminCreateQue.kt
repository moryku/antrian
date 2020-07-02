package com.abelherl.antrian

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import kotlinx.android.synthetic.main.activity_admin_create_que.*
import kotlinx.android.synthetic.main.list_act.view.*
import java.text.SimpleDateFormat
import java.util.*

class AdminCreateQue : AppCompatActivity() {

    private lateinit var actRef: DatabaseReference
    private lateinit var adapter: FirebaseRecyclerAdapter<Activity, AdminCreateQue.ListHolder>
    private lateinit var query: Query

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_create_que)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.isSmoothScrollbarEnabled = true
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_crt_req.layoutManager = layoutManager

        actRef = FirebaseDatabase.getInstance().getReference("Activity")
    }

    override fun onStart() {
        super.onStart()
        setList()
    }

    private fun setList() {
        query = actRef.orderByChild("status").equalTo("1")

        val option = FirebaseRecyclerOptions.Builder<Activity>()
            .setQuery(query, Activity::class.java)
            .setLifecycleOwner(this)
            .build()
        adapter = object : FirebaseRecyclerAdapter<Activity, AdminCreateQue.ListHolder>(option){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminCreateQue.ListHolder {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_act, parent, false)
                return ListHolder(view)
            }

            override fun onBindViewHolder(holder: AdminCreateQue.ListHolder, position: Int, model: Activity) {
                val context: Context = holder.itemView.context
                val actId: String? = getRef(position).key

                holder.bind(model)
                holder.itemView.setOnClickListener {
                    val matDialog = MaterialAlertDialogBuilder(context)
                    matDialog.setTitle("Join Activity")
                    matDialog.setMessage("Are you sure ?")
                    matDialog.setPositiveButton("Confirm") { dialog, which ->
                        createQue(actId!!)
                    }.setNegativeButton("cancel") { dialog, which ->
                    }
                    matDialog.show()
                }

            }
        }
        rv_crt_req.adapter = adapter
    }

    private fun createQue(actId: String) {
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        val date = formatter.format(Date())
        val getUid = FirebaseAuth.getInstance().currentUser?.uid
        val status = "0"
        val queue = Queue(getUid.toString(), actId, date, status)
        FirebaseDatabase.getInstance().getReference("Queue").push().setValue(queue)
            .addOnSuccessListener {
                toast("create success")
            }.addOnFailureListener {
                toast("create failed")
                createLog("create", it.message.toString())
            }
    }

    private fun createLog(s: String, message: String?) {
        Log.d(s, message)
    }

    private fun toast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    class ListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(activity: Activity) {
            itemView.tv_ls_act_title.text = activity.title
            itemView.tv_ls_act_name.text = activity.name
            itemView.tv_ls_act_desc.text = activity.description
        }
    }
}
