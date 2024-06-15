package uz.apphub.fayzullo.presentation.fragments

import android.content.Intent
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.apphub.fayzullo.R
import uz.apphub.fayzullo.databinding.FragmentHomeBinding
import uz.apphub.fayzullo.databinding.ScannerBinding
import uz.apphub.fayzullo.domain.model.MyApps
import uz.apphub.fayzullo.utils.AppPermissionsHelper
import uz.apphub.fayzullo.utils.VirusTotalApiService
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressBar: ProgressBar
    private lateinit var progressText: TextView
    private lateinit var helloText: TextView
    private lateinit var appName: TextView
    private lateinit var appIcon: ImageView
    private lateinit var resultText: TextView
    private var scannerBinding: ScannerBinding? = null

    private val virusTotalApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.virustotal.com/api/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VirusTotalApiService::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        binding.openPermission.setOnClickListener {
            openPermissionsManagementScreen()
        }
        lifecycleScope.launch {
            logicPlayMarket()
        }

        binding.scanner.setOnClickListener {
            scannerBinding = ScannerBinding.inflate(layoutInflater, binding.root, false)
            binding.root.addView(scannerBinding!!.root)

            progressBar = scannerBinding!!.progressBar
            progressText = scannerBinding!!.progressText
            helloText = scannerBinding!!.helloText
            appName = scannerBinding!!.appName
            appIcon = scannerBinding!!.appIcon
            resultText = scannerBinding!!.resultText

            scanInstalledApps()
        }
    }

    private fun openNewFragment(isPlayMarket: Int) {
        findNavController().navigate(
            R.id.action_homeFragment_to_appsFragment,
            bundleOf("isPlayMarket" to isPlayMarket)
        )
    }

    private fun scanInstalledApps() {
        val installedApps = AppPermissionsHelper.getInstalledApps(requireContext())
        val totalApps = installedApps.size
        var scannedApps = 0

        lifecycleScope.launch {
            val vulnerabilities = mutableListOf<MyApps>()

            for (app in installedApps) {
                // Update UI to show current app name and icon
                withContext(Dispatchers.Main) {
                    scannerBinding!!.appName.text = app.appName
                    scannerBinding!!.appIcon.setImageDrawable(app.appIcon) // Assuming 'app.icon' is a Drawable
                }

                val isVulnerable = checkAppForVulnerabilities(app)
                if (isVulnerable) {
                    vulnerabilities.add(app)

                }

                scannedApps++
                updateProgress(scannedApps, totalApps)
            }

            showScanResults(vulnerabilities)
        }
    }


    private suspend fun checkAppForVulnerabilities(app: MyApps): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = virusTotalApiService.scanUrl(
                    apiKey = "e83df9603e59926d38ec169c603fc3079fa30993cf57a4a002388aa15b84b9ba",
                    url = app.appName // Assuming app name as URL (modify as needed)
                )
                response.positives > 0
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("HomeFragment", "Error scanning app: ${e.message}")
                false
            }
        }
    }

    private fun updateProgress(scannedApps: Int, totalApps: Int) {
        val progress = (scannedApps * 100) / totalApps
        progressBar.progress = progress
        progressText.text = "$progress%"
    }

    private fun showScanResults( vulnerabilities: List<MyApps>) {
        if (vulnerabilities.isEmpty()) {
            helloText.text = vulnerabilities.toString()
            helloText.text = "Qurilma xavfsiz"
        } else {
            helloText.text = "Zaifliklar topildi: \n${vulnerabilities.joinToString { it.appName }}"
        }
        helloText.visibility = View.VISIBLE
        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        resultText.text = "Tekshiruv tugadi: $currentDate"
        resultText.visibility = View.VISIBLE
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
