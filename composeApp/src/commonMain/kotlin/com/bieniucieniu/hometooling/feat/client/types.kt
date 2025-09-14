package com.bieniucieniu.hometooling.feat.client

import io.ktor.http.Url
import io.ktor.utils.io.core.toByteArray
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.io.encoding.Base64
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Serializable
data class TokenBody(
    @SerialName("iss")
    val issuer: String,
    @SerialName("iat")
    val issuedAt: ULong,
    @SerialName("exp")
    val expire: ULong
)

interface WSBody {
    val type: String
}

@Serializable
data class WSBaseBody(
    @SerialName("type")
    override val type: String,
) : WSBody

@Serializable
data class WSAuthResponseBody(
    @SerialName("type")
    override val type: String = "auth",
    @SerialName("access_token")
    val accessToken: String
) : WSBody

fun Url?.isValidUrl(): Boolean = this != null

@OptIn(ExperimentalTime::class)
fun String.isValidToken(): Boolean {
    return try {
        defaultJsonConf.decodeFromString<TokenBody>(
            Base64.decode(this.split(".")[1].toByteArray()).toString()
        ).expire > Clock.System.now().toEpochMilliseconds().toULong()
    } catch (_: Throwable) {
        false
    }
}

sealed interface ClientStatus {
    object Idle : ClientStatus
    object Ready : ClientStatus
    class Error(val cause: Throwable) : ClientStatus
    class MissingState(val missingToken: Boolean, val missingHostUrl: Boolean) : ClientStatus
    object Loading : ClientStatus

    companion object {
        fun fromState(token: String?, hostUrl: Url?): ClientStatus {
            val missingToken = token == null || !token.isValidToken()
            val missingHostUrl = hostUrl == null || !hostUrl.isValidUrl()
            return if (missingToken || missingHostUrl) MissingState(
                missingToken = missingToken,
                missingHostUrl = missingHostUrl
            ) else Ready
        }
    }
}