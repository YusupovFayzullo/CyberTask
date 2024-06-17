package uz.apphub.fayzullo.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import uz.apphub.fayzullo.R
import uz.apphub.fayzullo.databinding.FragmentScannerBinding
import uz.apphub.fayzullo.domain.model.MyApps
import uz.apphub.fayzullo.utils.AppPermissionsHelper
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class ScannerFragment : Fragment(R.layout.fragment_scanner) {

    private var _binding: FragmentScannerBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressBar: ProgressBar
    private lateinit var progressText: TextView
    private lateinit var helloText: TextView
    private lateinit var appName: TextView
    private lateinit var appIcon: ImageView
    private lateinit var resultText: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentScannerBinding.bind(view)
            progressBar = binding.circularProgressBar
            progressText = binding.progressText
            helloText = binding.helloText
            appName = binding.appName
            appIcon = binding.appIcon
            resultText = binding.resultText

            scanInstalledApps()



    }
    private fun scanInstalledApps() {
        val installedApps = AppPermissionsHelper.getInstalledApps(requireContext())
        val totalApps = installedApps.size
        var scannedApps = 0

        lifecycleScope.launch {
            val vulnerabilities = mutableListOf<MyApps>()

            for (app in installedApps) {
                Log.d("VirusTotal", "App: ${app.appName}")

                // Update UI to show current app name and icon
                withContext(Dispatchers.Main) {
                    binding.appName.text = app.appName
                    binding.appIcon.setImageDrawable(app.appIcon) // Assuming 'app.icon' is a Drawable
                }

                val isDetected = checkAppForVulnerabilities(app)
                println("App: ${app.packageName}, Vulnerable: $isDetected")

                if (isDetected) {
                    vulnerabilities.add(app)
                }

                scannedApps++
                updateProgress(scannedApps, totalApps)
            }

            showScanResults(vulnerabilities)
        }
    }
    private fun updateProgress(scannedApps: Int, totalApps: Int) {
        val progress = (scannedApps * 100) / totalApps
        progressBar.progress = progress
        progressText.text = "$progress%"
    }

    private suspend fun checkAppForVulnerabilities(app: MyApps): Boolean {
        return suspendCoroutine { continuation ->
            isDetected(app.hashSHA256) { isDetect, message ->
                if (isDetect) {
                    continuation.resume(true)
                } else {
                    continuation.resume(false)
                }
            }
        }
    }
    private fun isDetected(fileHash:String, callback: (Boolean, String) -> Unit) {
        val apiKey = "e83df9603e59926d38ec169c603fc3079fa30993cf57a4a002388aa15b84b9ba"
        val fileHash = fileHash
        Log.d("VirusTotal", "File Hash: $fileHash")
        val client = OkHttpClient()

        // API so'rov URL manzili
        val url = "https://www.virustotal.com/api/v3/files/$fileHash"

        val request = Request.Builder()
            .url(url)
            .addHeader("x-apikey", apiKey)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false, "Failed to execute request: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("VirusTotal", "resp: $response")
                val body = response.body?.string()
                if (!response.isSuccessful || body.isNullOrBlank()) {
                    callback(false, "Failed to get valid response")
                    return
                }

                try {
                    val jsonObject = JSONObject(body)
                    val attributes = jsonObject.getJSONObject("data").getJSONObject("attributes")

                    val lastAnalysisStats = attributes.getJSONObject("last_analysis_stats")
                    val malicious = lastAnalysisStats.getInt("malicious")
                    val suspicious = lastAnalysisStats.getInt("suspicious")
                    val harmless = lastAnalysisStats.getInt("harmless")
                    Log.d("VirusTotal", "Malicious: $malicious")
                    Log.d("VirusTotal", "Suspicious: $suspicious")
                    Log.d("VirusTotal", "Harmless: $harmless")

                    val isDetected = malicious > 0 || suspicious > 0 || harmless > 0
                    Log.d("VirusTotal", "Is Detected: $isDetected")
                    val applicationName = attributes.getString("meaningful_name")
                    callback(isDetected, "Application Name: $applicationName")

                } catch (e: Exception) {
                    callback(false, "Failed to parse JSON response: ${e.message}")
                }
            }
        })
    }

    private fun showScanResults( vulnerabilities: List<MyApps>) {
        if (vulnerabilities.isEmpty()) {
            helloText.text = vulnerabilities.toString()
            helloText.text = "Qurilma xavfsiz"
        } else {
            helloText.text = "Zaifliklar  topildi: \n${vulnerabilities.joinToString { it.appName }}"
        }
        helloText.visibility = View.VISIBLE

        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        resultText.text = "Tekshiruv tugadi: $currentDate"
        resultText.visibility = View.VISIBLE
    }



}
