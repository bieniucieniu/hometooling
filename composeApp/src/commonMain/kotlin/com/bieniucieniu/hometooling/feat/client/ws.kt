package com.bieniucieniu.hometooling.feat.client

import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.websocket.Frame
import kotlinx.coroutines.channels.Channel

class WSChannel() {
    val incomingChan = Channel<Frame>()
    val outgoingsChan = Channel<Frame>()

    suspend fun DefaultClientWebSocketSession.listenIncoming() {
        while (true) {
            val a = incoming.receive()
            incomingChan.send(a)
        }
    }

    suspend fun DefaultClientWebSocketSession.listenOutgoing() {
        while (true) {
            val a = outgoingsChan.receive()
            outgoing.send(a)
        }
    }

    suspend fun send(o: Frame) = outgoingsChan.send(o)
    suspend fun receive(): Frame = incomingChan.receive()
}
