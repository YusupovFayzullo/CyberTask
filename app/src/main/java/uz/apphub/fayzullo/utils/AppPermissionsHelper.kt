package uz.apphub.fayzullo.utils

import android.content.Context
import android.content.pm.InstallSourceInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import uz.apphub.fayzullo.domain.model.MyApps
import uz.apphub.fayzullo.domain.model.PermissionModel
import java.io.FileInputStream
import java.security.MessageDigest


class AppPermissionsHelper {
    companion object {
        private const val TAG = "AppPermissionsHelper"
        private val appsList = mutableListOf<MyApps>()

        fun getInstalledApps(context: Context): List<MyApps> {
            if (appsList.isEmpty()) {
                appsList.clear()
                appsList.addAll(installedApps(context))
            }
            return appsList
        }
        fun getFileSha256(packageManager: PackageManager, packageName: String): String? {
            return try {
                val packageInfo: PackageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
                val signatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    packageInfo.signingInfo.apkContentsSigners
                } else {
                    TODO("VERSION.SDK_INT < P")
                }
                val messageDigest = MessageDigest.getInstance("SHA-256")

                for (signature in signatures) {
                    val digest = messageDigest.digest(signature.toByteArray())
                    val hexString = StringBuilder()
                    for (b in digest) {
                        val hex = Integer.toHexString(0xFF and b.toInt())
                        if (hex.length == 1) hexString.append('0')
                        hexString.append(hex)
                    }
                    return hexString.toString()
                }
                null
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
        fun getApkFilePath(packageManager: PackageManager, packageName: String): String? {
            return try {
                packageManager.getApplicationInfo(packageName, 0).sourceDir
            } catch (e: PackageManager.NameNotFoundException) {
                null
            }
        }
        fun calculateSHA256(filePath: String): String? {
            try {
                val digest = MessageDigest.getInstance("SHA-256")
                val fis = FileInputStream(filePath)
                val buffer = ByteArray(1024)
                var bytesRead: Int

                while (fis.read(buffer).also { bytesRead = it } != -1) {
                    digest.update(buffer, 0, bytesRead)
                }

                fis.close()
                val hashBytes = digest.digest()
                val sb = StringBuilder()

                for (b in hashBytes) {
                    sb.append(String.format("%02x", b))
                }

                return sb.toString()
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }


        private fun installedApps(context: Context): List<MyApps> {
            val installedAppsList = mutableListOf<MyApps>()
            val packageManager = context.packageManager
            val packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS)

            for (packageInfo in packageInfoList) {
                val packageName = packageInfo.packageName
                val permissionModels = mutableListOf<PermissionModel>()
                val fileSha256 = getFileSha256(packageManager, packageName)
                packageInfo.requestedPermissions?.forEach { permission ->
                    val isGranted = (packageManager.checkPermission(permission!!, packageName) == PackageManager.PERMISSION_GRANTED)
                    permissionModels.add(PermissionModel(permission, isGranted))
                }
                if (permissionModels.isNotEmpty() && !isSystemApp(packageManager, packageInfo)) {
                    try {
                        val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
                        val appName = packageManager.getApplicationLabel(applicationInfo).toString()
                        val appIcon = packageManager.getApplicationIcon(applicationInfo)
                        val isPlayMarket = isAppFromPlayMarket(context, packageName)
                        installedAppsList.add(MyApps(appName, appIcon, isPlayMarket, permissionModels,packageName,
                          fileSha256.toString()))
                    } catch (e: PackageManager.NameNotFoundException) {
                        e.printStackTrace()
                    }
                }
            }
            return installedAppsList
        }
//        fun installedFullApps(context: Context): List<MyApps> {
//            val installedAppsList = mutableListOf<MyApps>()
//            val packageManager = context.packageManager
//            val packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS)
//
//            for (packageInfo in packageInfoList) {
//                val packageName = packageInfo.packageName
//                val permissionModels = mutableListOf<PermissionModel>()
//                packageInfo.requestedPermissions?.forEach { permission ->
//                    val isGranted = (packageManager.checkPermission(permission!!, packageName) == PackageManager.PERMISSION_GRANTED)
//                    permissionModels.add(PermissionModel(permission, isGranted))
//                }
//                try {
//                    val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
//                    val appName = packageManager.getApplicationLabel(applicationInfo).toString()
//                    val appIcon = packageManager.getApplicationIcon(applicationInfo)
//                    val isPlayMarket = isAppFromPlayMarket(context, packageName)
//                    installedAppsList.add(MyApps(appName, appIcon, isPlayMarket, permissionModels,packageName,))
//                } catch (e: PackageManager.NameNotFoundException) {
//                    e.printStackTrace()
//                }
//            }
//            return installedAppsList
//        }

        private fun isSystemApp(packageManager: PackageManager, packageInfo: android.content.pm.PackageInfo): Boolean {
            val applicationInfo = packageInfo.applicationInfo
            return (applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_SYSTEM) != 0
        }
        private fun isAppFromPlayMarket(context: Context, packageName: String): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                try {
                    val packageManager = context.packageManager
                    val installSourceInfo: InstallSourceInfo =
                        packageManager.getInstallSourceInfo(packageName)
                    installSourceInfo.installingPackageName == "com.android.vending"
                } catch (e: Exception) {
                    Log.e(TAG, "isAppFromPlayMarket: ${e.message}", e)
                    false
                }
            } else {
                val packageManager = context.packageManager
                val installerPackageName = packageManager.getInstallerPackageName(packageName)
                installerPackageName == "com.android.vending"
            }
        }
    }
}


