package com.bieniucieniu.hometooling.feat.client

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bieniucieniu.hometooling.feat.storage.database.AppDatabase
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.http.Url
import io.ktor.http.buildUrl
import io.ktor.http.parseUrl
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import io.ktor.websocket.Frame
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.dsl.module

class Client(val db: AppDatabase, json: Json) : ViewModel() {
    val token = MutableStateFlow<String?>(null)
    val hostUrl = MutableStateFlow<Url?>(null)
    val wsPath = MutableStateFlow<Array<String>?>(null)
    val status = MutableStateFlow<ClientStatus>(ClientStatus.Idle)

    val state = object {
        val token @Composable get() = this@Client.token.collectAsState()
        val hostUrl @Composable get() = this@Client.hostUrl.collectAsState()
        val wsPath @Composable get() = this@Client.wsPath.collectAsState()
        val status @Composable get() = this@Client.status.collectAsState()
    }
    private val c = HttpClient {
        install(ContentNegotiation) {
            json(json)
        }
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(json)
            pingIntervalMillis = 20_000
        }
    }

    init {
        viewModelScope.launch {
            try {
                status.value = ClientStatus.Loading
                val kv = db.kvDao()
                token.value = kv.select(DBKeys.TOKEN)
                hostUrl.value = kv.select(DBKeys.HOST_URL)?.let { parseUrl(it) }
                wsPath.value = kv.select(DBKeys.WS_PATH)?.split(Regex("[\\\\/]"))?.toTypedArray()
                status.value = ClientStatus.fromState(token.value, hostUrl.value)
            } catch (e: Throwable) {
                status.value = ClientStatus.Error(e)
            }
        }
    }

    fun getUrl(vararg path: String): Url = buildUrl {
        takeFrom(hostUrl.value ?: throw IllegalStateException("Host url is not set"))
        pathSegments += path.toList()
    }

    fun HttpRequestBuilder.appendToken() {
        val token = token.value ?: throw IllegalStateException("Token is not set")
        headers.append("Authorization", "Bearer $token")
    }

    suspend fun get(vararg path: String, builder: HttpRequestBuilder.() -> Unit) =
        c.get(getUrl(path = path)) {
            builder()
            appendToken()
        }

    suspend fun post(vararg path: String, builder: HttpRequestBuilder.() -> Unit) =
        c.get(getUrl(path = path)) {
            builder()
            appendToken()
        }

    suspend fun delete(vararg path: String, builder: HttpRequestBuilder.() -> Unit) =
        c.delete(getUrl(path = path)) {
            builder()
            appendToken()
        }

    suspend fun webSockets(handler: suspend DefaultClientWebSocketSession.() -> Unit) {
        val url = getUrl(path = wsPath.value ?: ClientDefaults.wsPath)
        c.webSocketSession { url { takeFrom(url) } }.handler()
        c.webSocket(request = { url { takeFrom(url) } }) {
            handler()
        }
    }

    suspend fun DefaultClientWebSocketSession.handleWSAuthRequest() {
        var a by mutableStateOf(1)
        val body = WSAuthResponseBody(
            accessToken = token.value ?: throw IllegalStateException("Token is not set")
        )
        send(Frame.Text(defaultJsonConf.encodeToString(body)))
    }

    suspend fun handleLogOut() {
        token.value = null
        status.value = ClientStatus.fromState(token.value, hostUrl.value)
    }

    companion object {
        object DBKeys {
            const val TOKEN = "token"
            const val HOST_URL = "host_url"
            const val WS_PATH = "ws_path"
        }
    }
}

object ClientDefaults {
    val wsPath = arrayOf("api", "websocket")
}

val defaultJsonConf = Json {
    prettyPrint = true
    isLenient = true
}

val clientModule = module {
    single { defaultJsonConf }
    single { Client(get(), get()) }
}


