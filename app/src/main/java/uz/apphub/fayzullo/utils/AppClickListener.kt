package uz.apphub.fayzullo.utils

import uz.apphub.fayzullo.domain.model.MyApps

interface AppClickListener {
    fun onItemClick(app: MyApps)
}
