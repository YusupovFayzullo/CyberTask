package uz.apphub.fayzullo.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import uz.apphub.fayzullo.data.room.entity.SignatureEntity

/**Created By Begzod Shokirov on 11/06/2024 **/

@Dao
interface SignatureDao {
    @Insert
    suspend fun insertSignature(signature: SignatureEntity)
    @Insert
    suspend fun insertSignatures(signatures: List<SignatureEntity>)
    @Delete
    suspend fun deleteSignature(signature: SignatureEntity)
    @Delete
    suspend fun deleteSignatures(signatures: List<SignatureEntity>)
    @Query("SELECT * FROM SignatureEntity WHERE id=:id")
    suspend fun getSignature(id: String): SignatureEntity
}