package uz.apphub.fayzullo.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.apphub.fayzullo.domain.model.SignatureModel

@Entity
data class SignatureEntity(
    @PrimaryKey val id: String,
    val packageName: String,
    val appHash: String,
    val created: Long
)
 fun SignatureEntity.toSignatureModel(): SignatureModel {
     return SignatureModel(
         id = id,
         packageName = packageName,
         appHash = appHash,
         created = created
     )
 }
