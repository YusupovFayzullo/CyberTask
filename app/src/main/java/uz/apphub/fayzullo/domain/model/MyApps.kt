package uz.apphub.fayzullo.domain.model

import android.graphics.drawable.Drawable

data class MyApps(
    var appName: String,
    var appIcon: Drawable,
    val isPlayMarket: Boolean,
    var permissionModels: List<PermissionModel>,

)
