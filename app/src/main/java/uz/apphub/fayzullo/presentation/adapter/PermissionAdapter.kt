package uz.apphub.fayzullo.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import uz.apphub.fayzullo.R
import uz.apphub.fayzullo.domain.model.PermissionModel

class PermissionAdapter(private var permissionModels: List<PermissionModel>) :
    RecyclerView.Adapter<PermissionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PermissionViewHolder {
        return PermissionViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_permission, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PermissionViewHolder, position: Int) {
        val permission = permissionModels[position]
        holder.permission.text = permission.name
        holder.permission.isChecked = permission.isGranted
    }

    override fun getItemCount(): Int {
        return permissionModels.size
    }
}

class PermissionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var permission: CheckBox = itemView.findViewById(R.id.permission)
}
