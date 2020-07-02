package com.abelherl.antrian

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abelherl.antrian.dataclass.Activity
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import kotlinx.android.synthetic.main.activity_admin_manage_act.*
import kotlinx.android.synthetic.main.list_act.view.*

class AdminManageActivity : AppCompatActivity() {

    private lateinit var actRef: DatabaseReference
    private lateinit var adapter: FirebaseRecyclerAdapter<Activity, ListHolder>
    private lateinit var query: Query

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_manage_act)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.isSmoothScrollbarEnabled = true
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_mng_act.layoutManager = layoutManager

        setSupportActionBar(bar_mng_act)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        actRef = FirebaseDatabase.getInstance().getReference("Activity")

        fab_mng_act.setOnClickListener {
            startActivity(Intent(this, AdminCreateAct::class.java))
        }

    }

    override fun onStart() {
        super.onStart()
        query = actRef.orderByChild("status").equalTo("0")
        setList()
        adapter.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.stopListening()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_adm_act, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.all){
            query = actRef
            setList()
        }else if (item.itemId == R.id.open){
            query = actRef.orderByChild("status").equalTo("0")
            setList()
        }else if (item.itemId == R.id.close){
            query = actRef.orderByChild("status").equalTo("1")
            setList()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setList() {
        val option = FirebaseRecyclerOptions.Builder<Activity>()
            .setQuery(query, Activity::class.java)
            .setLifecycleOwner(this)
            .build()
        adapter = object : FirebaseRecyclerAdapter<Activity, ListHolder>(option){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_act, parent, false)
                return ListHolder(view)
            }

            override fun onBindViewHolder(holder: ListHolder, position: Int, model: Activity) {
                val context: Context = holder.itemView.context
                val actId: String? = getRef(position).key

                holder.bind(model)
                holder.itemView.setOnLongClickListener {
                    val matDialog = MaterialAlertDialogBuilder(context)
                    matDialog.setTitle("Closed Activity")
                    matDialog.setMessage("Are you sure ?")
                    matDialog.setPositiveButton("Confirm") { dialog, which ->
                        closeAct(actId!!)
                    }.setNegativeButton("cancel") { dialog, which ->

                    }
                    matDialog.show()
                    true
                }

                holder.itemView.setOnClickListener {
                    val intent = Intent(context, AdminUpdateAct::class.java)

                    intent.putExtra("actId", actId)
                    startActivity(intent)
                }

            }
        }
        rv_mng_act.adapter = adapter

    }

    private fun closeAct(position: String) {
        actRef.child(position).child("status").setValue("1")
            .addOnSuccessListener {
                toast("Activity Closed")
            }.addOnFailureListener { exception ->
                toast(exception.message.toString())
                createLog("update",exception.message.toString())
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
