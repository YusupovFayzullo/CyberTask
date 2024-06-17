package uz.apphub.fayzullo.presentation.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*;
import org.json.JSONObject
import uz.apphub.fayzullo.R
import uz.apphub.fayzullo.databinding.FragmentHomeBinding
import uz.apphub.fayzullo.domain.model.MyApps
import uz.apphub.fayzullo.utils.AppPermissionsHelper
import uz.apphub.fayzullo.utils.VirusTotalApi
import uz.apphub.fayzullo.utils.VirusTotalResponse
import uz.apphub.fayzullo.utils.api
import java.io.FileInputStream
import java.io.IOException
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        binding.openPermission.setOnClickListener {
            openPermissionsManagementScreen()
        }
        lifecycleScope.launch {
            logicPlayMarket()
        }


    }

    private fun openNewFragment(isPlayMarket: Int) {
        findNavController().navigate(
            R.id.action_homeFragment_to_appsFragment,
            bundleOf("isPlayMarket" to isPlayMarket)
        )
    }





    fun calculateSHA256(filePath: String): String? {
        return try {
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

            sb.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getVirusTotalReport(
        api: VirusTotalApi,
        apiKey: String,
        sha256Hash: String
    ): VirusTotalResponse? {
        return withContext(Dispatchers.IO) {
            try {
                api.getReport(apiKey, sha256Hash)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private fun logicPlayMarket() {
        var apps = 0
        var playApp = 0
        var otherApp = 0

        val installedApps = AppPermissionsHelper.getInstalledApps(requireContext())

        installedApps.forEach { app ->
            apps++
            if (app.isPlayMarket) {
                playApp++
            } else {
                otherApp++
            }
        }

        binding.playCount.text = playApp.toString()
        binding.appsCount.text = apps.toString()
        binding.noPlayCount.text = otherApp.toString()

        binding.noPlay.setOnClickListener {
            openNewFragment(2) // Other sources
        }
        binding.apps.setOnClickListener {
            openNewFragment(0) // Installed apps
        }
        binding.playApps.setOnClickListener {
            openNewFragment(1) // Play Market apps
        }
    }

    private fun openPermissionsManagementScreen() {
        val intent = Intent(Settings.ACTION_SECURITY_SETTINGS)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
