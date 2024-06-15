package uz.apphub.fayzullo.domain.model

data class SignatureModel(
    val id: String,
    val packageName: String,
    val appHash: String,
    val created: Long
)
