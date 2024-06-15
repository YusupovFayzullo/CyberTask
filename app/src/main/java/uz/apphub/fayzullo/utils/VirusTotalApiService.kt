package uz.apphub.fayzullo.utils



import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface VirusTotalApiService {
    @GET("url/scan")
    suspend fun scanUrl(
        @Header("x-apikey") apiKey: String,
        @Query("url") url: String
    ): VirusTotalResponse
}

data class VirusTotalResponse(
    val scan_id: String,
    val url: String,
    val scan_date: String,
    val permalink: String,
    val positives: Int,
    val total: Int
)
