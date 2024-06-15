package uz.apphub.fayzullo.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.apphub.fayzullo.R
import uz.apphub.fayzullo.domain.model.MyApps
import uz.apphub.fayzullo.utils.AppClickListener

class AppsAdapter() :
    RecyclerView.Adapter<AppsViewHolder>() {
    private var apps = mutableListOf<MyApps>()
    private var listener: AppClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppsViewHolder {
        return AppsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_apps, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AppsViewHolder, position: Int) {
        val app = apps[position]
        holder.appImage.setImageDrawable(app.appIcon)
        holder.appName.text = app.appName

        holder.item.setOnClickListener {
            listener?.onItemClick(app)
        }
    }

    override fun getItemCount(): Int {
        return apps.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initList(list: List<MyApps>) {
        apps.clear()
        apps.addAll(list)
        notifyDataSetChanged()
    }

    fun initListener(listener: AppClickListener) {
        this.listener = listener
    }
}

class AppsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var appImage: ImageView = itemView.findViewById(R.id.ivApp)
    var appName: TextView = itemView.findViewById(R.id.tvAppName)
    var item: LinearLayout = itemView.findViewById(R.id.appItem)
}

