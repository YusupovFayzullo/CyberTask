package uz.apphub.fayzullo.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PermissionModel(
    var name: String,
    var isGranted: Boolean
) : Parcelable
