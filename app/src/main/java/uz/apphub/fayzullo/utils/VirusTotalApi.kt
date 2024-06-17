package uz.apphub.fayzullo.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface VirusTotalApi {
    @GET("file/report")
    suspend fun getReport(
        @Header("x-apikey") apiKey: String,
        @Query("resource") resource: String
    ): VirusTotalResponse
}

data class VirusTotalResponse(
    val response_code: Int,
    val verbose_msg: String,
    val resource: String,
    val scan_id: String,
    val sha1: String,
    val sha256: String,
    val md5: String,
    val scans: Map<String, ScanResult>
)

data class ScanResult(
    val detected: Boolean,
    val version: String,
    val result: String,
    val update: String
)

val retrofit = Retrofit.Builder()
    .baseUrl("https://www.virustotal.com/vtapi/v2/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val api = retrofit.create(VirusTotalApi::class.java)
