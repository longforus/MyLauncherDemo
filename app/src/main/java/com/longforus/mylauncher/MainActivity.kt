package com.longforus.mylauncher

import android.content.*
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val activities = packageManager.queryIntentActivities(intent, 0)
        val adapter = MainAdapter(activities, this)
        rv.adapter = adapter
        rv.layoutManager = GridLayoutManager(this,4,RecyclerView.VERTICAL,false)
        val homeFilter = IntentFilter(
            Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        registerReceiver(homeReceiver, homeFilter)
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(homeReceiver)
    }

    private val homeReceiver = object : BroadcastReceiver() {
        internal val SYS_KEY = "reason" // 标注下这里必须是这么一个字符串值

        internal val SYS_HOME_KEY = "homekey"// 标注下这里必须是这么一个字符串值

        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == Intent.ACTION_CLOSE_SYSTEM_DIALOGS) {
                val reason = intent.getStringExtra(SYS_KEY)
                if (reason != null && reason == SYS_HOME_KEY) {
                    Log.i("TT", "##################home键监听")
                    val intentw = Intent(Intent.ACTION_MAIN)
                    intentw.addCategory(Intent.CATEGORY_HOME)
                    intentw.setClassName("android",
                        "com.android.internal.app.ResolverActivity")
                    startActivity(intentw)
                }
            }
        }
    }


    class ViewHolder(view: View,val context: Context):RecyclerView.ViewHolder(view){


        fun bindData(resolveInfo: ResolveInfo) {
            itemView.findViewById<ImageView>(R.id.iv).setImageDrawable(resolveInfo.loadIcon(context.packageManager))
            itemView.findViewById<TextView>(R.id.tv).text = resolveInfo.loadLabel(context.packageManager)
        }

    }


    class MainAdapter(val activities: MutableList<ResolveInfo>, val context: Context) :RecyclerView.Adapter<ViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
           return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_main,parent,false),context)
        }

        override fun getItemCount(): Int =activities.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bindData(activities[position])
            holder.itemView.setOnClickListener {
                val activityInfo = activities[position].activityInfo
                val cn = ComponentName(activityInfo.packageName, activityInfo.name)
                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_LAUNCHER)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.component = cn
                context.startActivity(intent)
            }
        }

    }
}
