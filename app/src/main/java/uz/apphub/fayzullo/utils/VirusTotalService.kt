package uz.apphub.fayzullo.utils

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface VirusTotalService {

    @Headers("x-apikey: e83df9603e59926d38ec169c603fc3079fa30993cf57a4a002388aa15b84b9ba")
    @GET("file/report")
    suspend fun scanApp(
        @Query("apikey") apiKey: String,
        @Query("resource") resource: String
    ): VirusTotalResponse
}
