package com.bieniucieniu.hometooling.feat.ha.ws.messages

import com.bieniucieniu.hometooling.feat.client.defaultJsonConf
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

val messageSerializer = SerializersModule {
    polymorphic(HaMessage::class) {
        subclass(Auth::class)
        subclass(AuthInvalid::class)
        subclass(AuthOk::class)
        subclass(AuthRequired::class)
        subclass(CallService::class)
        subclass(Event::class)
        subclass(FireEvent::class)
        subclass(GetConfig::class)
        subclass(GetPanels::class)
        subclass(GetServices::class)
        subclass(GetStates::class)
        subclass(Ping::class)
        subclass(Pong::class)
        subclass(Result::class)
        subclass(SubscribeEvents::class)
        subclass(SubscribeTrigger::class)
        subclass(SupportedFeatures::class)
        subclass(UnsubscribeEvents::class)
        subclass(ValidateConfig::class)
    }
}
val haJson = Json(defaultJsonConf) {
    serializersModule = messageSerializer
    classDiscriminator = "type"
}
